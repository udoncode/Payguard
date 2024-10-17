import { drawChart as drawPieChart } from "./chart/pie.js";
import { drawChart as drawColumnChart } from "./chart/column.js";
import { drawChart as drawLineChart } from "./chart/line.js";

document.querySelector('form').addEventListener('submit', (e) => {
	e.preventDefault();
});

const renderAnalysis = () => {
	const budgetId = document.getElementById('budget_id').value;

	if (!budgetId) {
		alert('예산을 선택해 주세요!');
		return;
	}

	const curveChart = document.getElementById('curve_chart');
	const columnChart = document.getElementById('column_chart');
	const pieChart = document.getElementById('pie_chart');
	const charts = [curveChart, columnChart, pieChart];

	// 차트 숨기기
	charts.forEach(chart => {
		if (!chart.classList.contains('hide')) {
			chart.classList.add('hide');
		}
	})

	// 스피너 보이기
	const loaders = document.querySelectorAll('.loader');
	loaders.forEach(loader => {
		if (loader.classList.contains('hide')) {
			loader.classList.remove('hide');
		}
	})


	// 비동기 요청
	fetch(`/analysis/budget?id=${budgetId}`)
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(data => {
			document.querySelector('.progress-bar .chart p > span').innerHTML = `${data.progressPercent}%`;
			document.querySelector('.progress-bar .chart .bar > .progress').style.width = `${data.progressPercent}%`;
			document.querySelector('.budget-amount p').innerHTML = data.amount.toLocaleString();
			document.querySelector('.remaining-amount p').innerHTML = data.expense.toLocaleString();


			drawPieChart(data.categoryCounts);
			drawColumnChart(data.recentBudgets);
			drawLineChart(data.transactionAmounts);

			
			const test = setInterval(() => {
				const hiddenSpans = document.querySelectorAll('span[aria-hidden="true"]');
				
				if (hiddenSpans.length === 0) {
					// 차트 보이기
					charts.forEach(chart => {
						if (chart.classList.contains('hide')) {
							chart.classList.remove('hide')
						}
					})

					// 스피너 숨기기
					loaders.forEach(loader => {
						if (!loader.classList.contains('hide')) {
							loader.classList.add('hide');
						}
					})
					
					// 호출 종료
					clearInterval(test)
				}
			}, 1000)

		})
		.catch(error => console.error('Error:', error))

}

document.querySelector('form .btn--search').addEventListener('click', renderAnalysis);