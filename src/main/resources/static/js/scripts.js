
function Enroll(url) {
  swal({
    title: "Are you sure you want to enroll this course?",
    text: "Once you enroll, you can leave if you want.",
    buttons: true,
    dangerMode: true
  }).then(willAccept => {
    if (willAccept) {
      $.ajax(
        {
          type: "POST",
          url: url,
          success: function(data) {
            if (data) {
              window.location.href = "/";
            }
          },
          error: function(exception) {
            window.location.href = "/error";
          }
        }
      )
    }
  });
}

function Delete(url) {
  swal({
    title: "Are you sure you want to delete?",
    text: "You will not able to restore this data",
    icon: "warning",
    buttons: true,
    dangerMode: true
  }).then((willDelete) => {
    if (willDelete) {
      $.ajax(
        {
          type: "DELETE",
          url: url,
          success: function (data) {
            if (data) {
              location.reload();
              console.log("Delete operation is successful");
            } else {
              console.log("Error after delete");
            }
          }
        }
      )
    } else {
      console.log("No operation!");
    }
  });
}