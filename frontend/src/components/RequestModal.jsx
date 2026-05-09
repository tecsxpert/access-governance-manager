function RequestModal({
  request,
  onClose,
}) {

  if (!request) return null;

  return (

    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor:
          "rgba(0,0,0,0.5)",

        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >

      <div
        style={{
          backgroundColor: "white",
          padding: "30px",
          width: "400px",
          borderRadius: "10px",
        }}
      >

        <h2>Request Details</h2>

        <p>
          <b>ID:</b> {request.id}
        </p>

        <p>
          <b>User:</b> {request.userName}
        </p>

        <p>
          <b>Resource:</b>
          {" "}
          {request.resourceName}
        </p>

        <p>
          <b>Access Type:</b>
          {" "}
          {request.accessType}
        </p>

        <p>
          <b>Status:</b>
          {" "}
          {request.status}
        </p>

        <button
          onClick={onClose}
          style={{
            marginTop: "20px",
            padding: "10px",
            cursor: "pointer",
          }}
        >
          Close
        </button>

      </div>

    </div>
  );
}

export default RequestModal;