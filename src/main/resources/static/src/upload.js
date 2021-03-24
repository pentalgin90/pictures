var url = "http://54.175.179.65:8080";

function uploadImage() {
  addImage();
}

function addImage() {
	var formData = new FormData();
	formData.append('imageFile', $('#image')[0].files[0]);

	$.ajax({
		   url : url + "/images",
		   type : 'POST',
		   data : formData,
		   processData: false,
		   contentType: false,
		   success : function(data) {
			   $('#image')[0].value = "";
			   loadAllImages();
		   }
	});
}

function deleteImage(imageName) {
	$.ajax({
		   url : url + "/images/" + imageName,
		   type : 'DELETE',
		   processData: false,
		   contentType: false,
		   success : function(data) {
			   loadAllImages();
		   }
	});
}

function generateImageHtml(image, index) {
  return `
	<li class="list-group-item checkbox">
	  <div class="row">
		<div class="col-md-10 col-xs-10 col-lg-10 col-sm-10 task-text">
		  ${image.name}
		</div>
		<div class="col-md-1 col-xs-1 col-lg-1 col-sm-1 delete-icon-area">
		  <a class="" href="${image.path}" target="_blank"><i id="openImage" data-id="${index}" class="view-icon glyphicon glyphicon-file"></i></a>
		  <a style="margin-left:10px" class="" href="#" onclick="deleteImage('${image.id}');return false;"><i id="deleteImage" data-id="${image.id}" class="view-icon glyphicon glyphicon-trash"></i></a>
		</div>
	  </div>
	</li>
  `;
}

function loadAllImages() {
	$.ajax({
		   url : url + "/images",
		   type : 'GET',
		   processData: false,
		   contentType: false,
		   success : function(data) {
			   buildImagesTable(data);
		   }
	});
}

function buildImagesTable(data) {
  let imagesHtml = data.reduce((html, image, id) => html += generateImageHtml(image, id), '');
  document.getElementById('imageList').innerHTML = imagesHtml;
}

window.addEventListener("load", () => {
    loadAllImages()
});
