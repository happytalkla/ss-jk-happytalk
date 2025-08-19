$(function(){

    tinymce.init({
        selector: '.editor-contents',
        plugins: ["image", "table", "textcolor", "code"],
        menubar: false,
        toolbar: "undo redo | styleselect | forecolor bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | table link media custom_image code ",
        paste_data_images: true,
        images_upload_handler: function (blobInfo, success, failure) {
            // no upload, just return the blobInfo.blob() as base64 data
            success("data:" + blobInfo.blob().type + ";base64," + blobInfo.base64());
        },
        setup: function (editor) {
            var inp = $('<input id="tinymce-uploader" type="file" name="pic" accept="image/*" style="display:none">');
            $(editor.getElement()).parent().append(inp);

            inp.on("change", function () {
                var input = inp.get(0);
                var file = input.files[0];
                var fr = new FileReader();
                fr.onload = function () {
                    var img = new Image();
                    img.src = fr.result;
                    editor.execCommand('mceInsertContent', true, '<img src="' + img.src + '"/>');

                    //tinymce.activeEditor.execCommand("mceInsertContent",'false','');

                }
                fr.readAsDataURL(file);
            });

            editor.addButton('custom_image', {
                text: false,
                icon: "image",
                onclick: function (e) {
                    inp.trigger('click');
                }
            });
        }
    });

});