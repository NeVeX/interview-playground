$(document).ready(function() {

    function goToSelectedItemWebpage() {
        // Need the abbreviation
        var itemSelected = $('#input_selection').val();

        console.log("Item Selected: "+itemSelected)

        if ( !itemSelected) { // empty or null
            return;
        }

        var dataList = $('#selection_data_list');
        var dataListEntry = $(dataList).find('option[value="' + itemSelected + '"]');
        var selectionHtmlValue = dataListEntry.attr('data-value');
        if ( selectionHtmlValue ) {
            location.href = selectionHtmlValue;
        }
    }

    $('#input_selection').keydown(function(event){
        var keyCode = (event.keyCode ? event.keyCode : event.which);
        if (keyCode == 13) { // Enter key code
            goToSelectedItemWebpage();
        }
    });
    $('#input_selection').bind('input', goToSelectedItemWebpage);
    $('#selection_data_list').bind('input', goToSelectedItemWebpage);
});

