/**
 * Created by Kushal on 25-Sep-16.
 */


function setYears(selectId) {
    var select = $("#" + selectId)

    $.each(choices, function(a, b) {
        select.append($("<option/>").attr("value", b).text(b));
    });
}