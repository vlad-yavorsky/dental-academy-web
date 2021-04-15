$(function () {
    /**
     * warning!
     * variables 'orderNumber' and 'STATUS_CREATED' should be defined in files, where this script is included
     */
    function refreshOrderStatus() {
        $.get("/api/order/" + orderNumber + "/status", function (orderStatus) {
            // todo: onSuccess, onFail
            if (orderStatusInitial !== orderStatus) {
                $("#order-status").html(orderStatus);
                $("#order-status-badge").show().text("changed").fadeOut(9000);
                orderStatusInitial = orderStatus;
            }
            if (orderStatus !== STATUS_CREATED) {
                $(".liqpay-button").hide();
            }
        });
    }
    refreshOrderStatus();
    setInterval(refreshOrderStatus, 10000);
});
