import React from "react";

import { toast, ToastContainer } from "react-toastify";

export const Test = () => {
  const notify = () =>
    toast.error("🦄 Wow so easy!", {
      position: "top-center",
      autoClose: 1000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "colored",
    });

  return (
    <div className="check">
      <button onClick={notify}>Make me a toast</button>
      <ToastContainer
        position="top-center"
        autoClose={1000}
        limit={1}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </div>
  );
};

const check = {
  height: "calc(var(--vh, 1vh) * 100",
};

// height: calc(var(--vh, 1vh) * 100);
// height: calc(var(--vh, 1vh) * 100 + [footer의 높이]);

// // 예: footer 높이가 66px인 경우
// height: calc(var(--vh, 1vh) * 100 + 66px);
