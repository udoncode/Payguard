google.charts.load('current', {packages: ['corechart', 'bar']});
google.charts.setOnLoadCallback(drawChart);

function drawChart(recentBudgets) {
  var data = google.visualization.arrayToDataTable([
    ['예산명', '예산 금액', { role: 'style' } ],
    ...recentBudgets
  ]);

  var options = {
    // title: '최근 설정한 예산',
    titleTextStyle: {
      color: '#0A0F2D',
      fontName: 'Prentendard Variable',
      fontSize: 20,
    },
    legend: { position: 'none' },
    hAxis: {
      title: '예산명',
      textStyle: {
        color: '#0A0F2D',
        fontName: 'Prentendard Variable',
        italic: false,
      },
    },
    vAxis: {
      title: '예산 금액',
      textStyle: {
        color: '#0A0F2D',
        fontName: 'Prentendard Variable',
        italic: false,
      },
    }
  };

  var chart = new google.visualization.ColumnChart(
    document.getElementById('column_chart'));

  chart.draw(data, options);
}

export { drawChart };