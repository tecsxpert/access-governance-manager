function ProfileCard() {

  const role =
    localStorage.getItem("role");

  return (

    <div
      style={{
        border: "1px solid gray",
        padding: "20px",
        borderRadius: "10px",
        marginBottom: "20px",
        width: "300px",
      }}
    >

      <h2>Profile</h2>

      <p>
        <b>Role:</b> {role}
      </p>

      <p>
        <b>Status:</b> Active
      </p>

    </div>
  );
}

export default ProfileCard;
