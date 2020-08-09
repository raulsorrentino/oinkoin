import 'package:flutter/material.dart';
import 'package:piggybank/models/record.dart';
import 'package:charts_flutter/flutter.dart' as charts;
import './i18n/statistics-page.i18n.dart';

class LinearRecord {
  final String category;
  final double value;

  LinearRecord(this.category, this.value);
}

class PieChartCard extends StatelessWidget {

  final List<Record> records;
  List<LinearRecord> linearRecords;
  List<charts.Series> seriesList;

  PieChartCard(this.records) {
    seriesList = _prepareData(records);
  }

  List<charts.Series<LinearRecord, String>> _prepareData(List<Record> records) {
    Map<String, double> aggregatedCategoriesValuesTemporaryMap = new Map();
    double totalSum = 0;
    for (var record in records) {
      totalSum += record.value.abs();
      aggregatedCategoriesValuesTemporaryMap.update(
          record.category.name, (value) => value + record.value.abs(),
          ifAbsent: () => record.value.abs());
    }
    var aggregatedCategoriesAndValues = aggregatedCategoriesValuesTemporaryMap
        .entries.toList();
    aggregatedCategoriesAndValues.sort((b, a) => a.value.compareTo(b.value)); // sort descending

    var limit = aggregatedCategoriesAndValues.length > categoryCount
        ? categoryCount
        : aggregatedCategoriesAndValues.length;

    var topCategoriesAndValue = aggregatedCategoriesAndValues.sublist(0, limit);

    // add top categories
    List<LinearRecord> data = [];
    for (var categoryAndValue in topCategoriesAndValue) {
      var percentage = (100 * categoryAndValue.value) / totalSum;
      var lr = LinearRecord(categoryAndValue.key, percentage);
      data.add(lr);
    }

    // if visualized categories are less than the total amount of categories
    // aggregated the reaming category as a mock category name "Other"
    if (limit < aggregatedCategoriesAndValues.length) {
      var remainingCategoriesAndValue = aggregatedCategoriesAndValues.sublist(limit);
      var sumOfRemainingCategories = remainingCategoriesAndValue.fold(0, (value, element) => value + element.value);
      var remainingCategoryKey = "Others".i18n;
      var percentage = (100 * sumOfRemainingCategories) / totalSum;
      var lr = LinearRecord(remainingCategoryKey, percentage);
      data.add(lr);
    }

    linearRecords = data;

    return [
      new charts.Series<LinearRecord, String>(
        id: 'Expenses'.i18n,
        colorFn: (LinearRecord sales, i) =>
          palette[i].shadeDefault,
        domainFn: (LinearRecord records, _) => records.category,
        measureFn: (LinearRecord records, _) => records.value,
        labelAccessorFn: (LinearRecord row, _) => row.category,
        data: data,
      )
    ];
  }

  bool animate = true;
  static final categoryCount = 5;
  static final palette = charts.MaterialPalette.getOrderedPalettes(categoryCount);

  Widget _buildPieChart() {
    return new Container(
        child: new charts.PieChart(
          seriesList,
          animate: animate,
          defaultRenderer: new charts.ArcRendererConfig(arcWidth: 35),
        )
    );
  }

  Widget _buildLegend() {
    /// Returns a ListView with all the movements contained in the MovementPerDay object
    return ListView.builder(
        physics: const NeverScrollableScrollPhysics(),
        shrinkWrap: true,
        itemCount: linearRecords.length,
        padding: const EdgeInsets.all(6.0),
        itemBuilder: /*1*/ (context, i) {
          var linearRecord = linearRecords[i];
          var recordColor = palette[i].shadeDefault;
          return Container(
            margin: EdgeInsets.fromLTRB(0, 0, 8, 8),
            child: new Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Container(
                  child: Row(
                    children: <Widget>[
                      Container(
                          height: 10,
                          width: 20,
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: Color.fromARGB(recordColor.a, recordColor.r, recordColor.g, recordColor.b),
                          )
                      ),
                      Text(linearRecord.category),
                    ],
                  )
                ),
                Text(linearRecord.value.toStringAsFixed(2) + " %"),
              ],
            )
          );
        });
  }

  Widget _buildCard() {
    return Container(
        padding: const EdgeInsets.fromLTRB(10, 8, 10, 0),
        height: 200,
        child: new Card(
            elevation: 2,
            child: new Row(
              children: <Widget>[
                Expanded(child: _buildPieChart()),
                Expanded(child: _buildLegend())
              ],
            )
        )
    );
  }

  @override
  Widget build(BuildContext context) {
    return _buildCard();
  }
}