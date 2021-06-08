$(function() {
    // update ordering for each item before form submit
    $("form").submit(function (event) {
        $(".sortable").find(".database-ordering").each(function (index) {
            $(this).val(index);
        });
    });
});
