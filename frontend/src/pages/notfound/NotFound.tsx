import React from "react";
import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div style={{ textAlign: "center", padding: "40px" }}>
      <h1 style={{ fontSize: "48px", marginBottom: "20px" }}>404</h1>
      <h2>페이지를 찾을 수 없습니다.</h2>
      <p style={{ fontSize: "24px", color: "#666" }}>Page not found</p>

      <Link to="/">Main Page</Link>
    </div>
  );
};

export default NotFound;
