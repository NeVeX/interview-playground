$(document).ready(function() {

    /**
     * This function will get the current selected item in the data list, retrieve the value of that data entry
     * which will be the HTML file location - and then if found, naviage to that html file
     */
    function goToSelectedItemWebpage() {
        var itemSelected = $('#input_selection').val();

        if ( !itemSelected) { // empty or null
            return;
        }

        var dataList = $('#selection_data_list'); // Get the full data list
        var dataListEntry = $(dataList).find('option[value="' + itemSelected + '"]'); // Find our selected item
        var selectionHtmlValue = dataListEntry.attr('data-value'); // Get the value (html) location
        if ( selectionHtmlValue ) {
            // If we have a html value, then navigate to that resource
            location.href = selectionHtmlValue;
        }
    }

    // Create a hook into the key down, if the enter key is hit, see if we should navigate to a resource
    $('#input_selection').keydown(function(event){
        var keyCode = (event.keyCode ? event.keyCode : event.which);
        if (keyCode == 13) { // Enter key code
            goToSelectedItemWebpage();
        }
    });

    // Create a hook into the input events to see if we should navigate to a resource
    $('#input_selection').bind('input', goToSelectedItemWebpage);
    $('#selection_data_list').bind('input', goToSelectedItemWebpage);

});
