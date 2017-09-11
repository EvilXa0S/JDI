var editCase = new Vue({
    el: '#editCase',
    data() {
        return{}
    },
    methods: {
        editCase: function() {
            axios.post('/updateCase/' + $("#suitId").text(), {description: $("#editDescriptionCase").val()}).then(function(response) {
                PopUpHide("#popup_edit_case");
                getSuits.getSuits();
            }).catch(function(error) {
            });
        },
    },
    watch: {
        message: function() {
            console.log(this.message)
        }
    }
});