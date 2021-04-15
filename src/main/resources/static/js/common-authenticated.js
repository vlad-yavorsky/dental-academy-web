$(function () {
    $.get("/api/me").done(function (user) {
        $("#avatar").attr("src", user.existingPhotoPath);
        $("#cart-items").text(user.cartItemsCount);
    });
});
