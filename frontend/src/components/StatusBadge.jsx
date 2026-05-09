function StatusBadge({ status }) {

  let color = "";

  if (status === "PENDING") {

    color = "orange";

  } else if (
    status === "APPROVED"
  ) {

    color = "green";

  } else {

    color = "red";
  }

  return (

    <span
      style={{
        backgroundColor: color,
        color: "white",
        padding: "5px 10px",
        borderRadius: "15px",
      }}
    >
      {status}
    </span>
  );
}

export default StatusBadge;