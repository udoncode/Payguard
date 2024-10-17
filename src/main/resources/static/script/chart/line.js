google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart(transactionAmounts) {
  var data = google.visualization.arrayToDataTable([
    ['거래 날짜', '거래 금액'],
    ...transactionAmounts
  ]);

  var options = {
    // title: '거래 금액 변화 추이',
    legend: { position: 'none' },
    titleTextStyle: {
      color: '#0A0F2D',
      fontName: 'Prentendard Variable',
      fontSize: '20'
    },
    curveType: 'function',
    colors: ['#1446FF'],
    hAxis: {
      title: '거래 날짜',
      textStyle: {
        color: '#0A0F2D',
        fontName: 'Prentendard Variable',
        italic: false,
      },
      titleTextStyle: {
        color: '#0A0F2D',
        fontName: 'Prentendard Variable',
        fontSize: '15',
        bold: true,
        italic: false,
      }
    },
    vAxis: {
      title: '거래 금액',
      textStyle: {
        color: '#0A0F2D',
        fontName: 'Prentendard Variable',
        italic: false,
      },
      titleTextStyle: {
        color: '#0A0F2D',
        fontName: 'Prentendard Variable',
        fontSize: '15',
        bold: true,
        italic: false,
      }
    },
  };

  var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

  chart.draw(data, options);
}

export { drawChart };