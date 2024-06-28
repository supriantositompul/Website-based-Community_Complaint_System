function addCSRF() {
    let token = $("meta[name='_csrf']").attr("content");
    console.log(token);
    let header = $("meta[name='_csrf_header']").attr("content");
    console.log(header);
    $(document).ajaxSend(function (e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
  }
  