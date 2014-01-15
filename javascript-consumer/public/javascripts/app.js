var log = function(msg) {
    $("body").append("<h1>"+msg+"</h1>");
};

App = {
    getAnimals: function() {
        var endpoint = $(".endpoint").val()+"/animals";
        log("click"+endpoint);
        $.get(endpoint, function(data) {
            log("retrieved");
            $.each(data, function(i, animal) {$(".animals").text(animal.name);});
        }).fail(
            function(xhr, text, error) {
                log("failed");
            $(".animals").text("Bob retrieval failed! text='" + text + "' error='" + error + "' status='" + xhr.status + "' response='" + xhr.responseText + "'");
            console.log("error: %o %o %o", xhr, text, error)
        });
    }
};

$(function() {
    log("loaded");
    $(".getAnimals").on("click", App.getAnimals);
    log("bound");
});