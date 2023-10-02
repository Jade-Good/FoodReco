import React, { useState, useEffect } from "react";
import Modal from "react-modal";
import { useRecoilState } from "recoil";
import { foodDetailModal } from "../../recoil/atoms/modalState";
import { Container as MapDiv, NaverMap, Marker } from "react-naver-maps";

/*overlay는 모달 창 바깥 부분을 처리하는 부분이고,
content는 모달 창부분이라고 생각하면 쉬울 것이다*/
const customModalStyles: ReactModal.Styles = {
  overlay: {
    backgroundColor: " rgba(0, 0, 0, 0.4)",
    width: "100%",
    height: "100vh",
    zIndex: "10",
    position: "fixed",
    top: "0",
    left: "0",
  },
  content: {
    width: "70vw",
    height: "70vh",
    // zIndex: "150",
    position: "absolute",
    top: "50%",
    left: "50%",

    padding: "1rem",

    transform: "translate(-50%, -50%)",
    borderRadius: "1rem",
    boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
    backgroundColor: "white",
    justifyContent: "center",
    overflow: "auto",
  },
};

interface Coords {
  latitude: number | null;
  longitude: number | null;
}

const FoodDetail = () => {
  const [modal, setModalOpen] = useRecoilState(foodDetailModal);

  let foodName = "짜장면";

  const [coords, setCoords] = useState<Coords>({ latitude: null, longitude: null });
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // 위치 정보 가져오기 시도
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setCoords({ latitude, longitude });
        },
        (err) => {
          setError(`오류: ${err.message}`);
        }
      );
    } else {
      setError("브라우저에서 Geolocation을 지원하지 않습니다.");
    }
  }, []);

  return (
    <Modal
      isOpen={modal.modalOpen}
      onRequestClose={() => setModalOpen({ modalOpen: false })}
      style={customModalStyles}
      ariaHideApp={false}
      contentLabel="Pop up Message"
      shouldCloseOnOverlayClick={true}
    >
      <h4 style={{ margin: "0" }}>주변 '{foodName}' 가게</h4>

      <div>
        {coords.latitude !== null && coords.longitude !== null ? (
          <div>
            <p>현재 위치:</p>
            <p>위도: {coords.latitude}</p>
            <p>경도: {coords.longitude}</p>
          </div>
        ) : error ? (
          <p>{error}</p>
        ) : (
          <p>위치 정보를 가져오는 중...</p>
        )}
      </div>

      <MapDiv
        style={{
          height: 400,
        }}
      >
        <NaverMap>
          <Marker defaultPosition={{ lat: 37.5666103, lng: 126.9783882 }} />
        </NaverMap>
      </MapDiv>
    </Modal>
  );
};

export default FoodDetail;

// isOpen
// 모달 창이 표시되어야 하는지 여부를 설명하는 boolean 값이다.
// 즉, 해당 값이 true여야 모달 창이 열리는 것이다.

// onRequestClose
// 모달이 닫힐 때 실행될 함수를 의미한다.
// 즉,사용자가 모달을 닫으려고 할 때 실행되는 함수이다.

// style
// 모달 창과 모달 창 바깥에 대한 style을 지정해준다.

// ariaHideApp
// appElement를 숨길지 여부를 나타내는 boolean 값입니다.
// 이 값이 true이면 appElement가 숨겨준다.

// contentLabel
// 스크린리더 사용자에게 콘텐츠를 전달할 때
// 사용되는 방법을 나타내는 문자열이다.

// shouldCloseOnOverlayClick
//팝업창이 아닌 바깥 부분에서 클릭하였을 때, 닫히도록 할 것인지에 대한 처리이다.
// 기본값으로는 true를 가지고 있다.
