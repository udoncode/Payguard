google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(() => drawChart([]));

function drawChart(categoryCounts) {

  var data = google.visualization.arrayToDataTable([
    ['카테고리', '개수'],
    ...categoryCounts
  ]);

  var options = {
    // title: '카테고리별 거래량',
    legend: { position: 'none' },
    slices: {
      0: { color: '#1446FF' },
      1: { color: '#0066FF' },
      2: { color: '#55AAFF' },
      3: { color: '#77CCFF' }
    }
  };

  var chart = new google.visualization.PieChart(document.getElementById('pie_chart'));

  chart.draw(data, options);
}

export { drawChart };