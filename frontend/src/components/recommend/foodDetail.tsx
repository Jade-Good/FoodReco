import React, { useState, useEffect } from "react";
import axios from "axios";

import Modal from "react-modal";
import { useRecoilState } from "recoil";
import { foodDetailModal } from "../../recoil/atoms/modalState";

import { Map } from "react-kakao-maps-sdk";

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
    width: "80vw",
    height: "80vh",
    // zIndex: "150",
    position: "absolute",
    top: "50%",
    left: "50%",

    padding: "0",

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

interface Result {
  address_name?: string;
  category_group_code?: string;
  category_group_name?: string;
  category_name?: string;
  distance?: string;
  id?: string;
  phone?: string;
  place_name?: string;
  place_url?: string;
  road_address_name?: string;
  x?: string;
  y?: string;
}

const FoodDetail = () => {
  const [modal, setModalOpen] = useRecoilState(foodDetailModal);

  let foodName = "짜장면";
  const ps = new kakao.maps.services.Places();
  // 키워드로 장소를 검색합니다
  // searchPlaces();

  // 장소검색이 완료됐을 때 호출되는 콜백함수
  function placesSearchCB(data: any, status: any, pagination: any) {
    if (status === kakao.maps.services.Status.OK) {
      console.log(data);
      setStores(data);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
      console.log("검색 결과가 존재하지 않습니다.");
      return;
    } else if (status === kakao.maps.services.Status.ERROR) {
      console.log("검색 결과 중 오류가 발생했습니다.");
      return;
    }
  }

  const [coords, setCoords] = useState<Coords>({ latitude: null, longitude: null });
  const [error, setError] = useState<string | null>(null);
  const [stores, setStores] = useState<Result[] | null>(null);

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
    search();
  }, []);

  const search = () => {
    console.log("짜장면집 찾아보자");
    ps.keywordSearch(foodName, placesSearchCB, {
      category_group_code: "FD6",
      x: Number(`${coords.longitude}`),
      y: Number(`${coords.latitude}`),
      radius: 1000,
    });
  };

  return (
    <Modal
      isOpen={modal.modalOpen}
      onRequestClose={() => setModalOpen({ modalOpen: false })}
      style={customModalStyles}
      ariaHideApp={false}
      contentLabel="Pop up Message"
      shouldCloseOnOverlayClick={true}
    >
      <h4 style={{ margin: "1rem 0rem 1rem 1rem" }}>주변 '{foodName}' 가게</h4>

      <button onClick={search}> 짜장면집 검색</button>

      <Map // 지도를 표시할 Container
        id="map"
        center={{
          // 지도의 중심좌표
          lat: Number(`${coords.latitude}`),
          lng: Number(`${coords.longitude}`),
        }}
        style={{
          // 지도의 크기
          width: "80vw",
          height: "20vh",
        }}
        level={7} // 지도의 확대 레벨
      />

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

      <hr />

      {stores
        ? stores.map((result, idx) => {
            return (
              <>
                <ul>
                  <li>주소이름 : {result.address_name}</li>
                  <li>그룹코드 : {result.category_group_code}</li>
                  <li>그룹이름 : {result.category_group_name}</li>
                  <li>카테고리 : {result.category_name}</li>
                  <li>거리 : {result.distance}</li>
                  <li>아이디 : {result.id}</li>
                  <li>전화번호 : {result.phone}</li>
                  <li>장소명 : {result.place_name}</li>
                  <li>url : {result.place_url}</li>
                  <li>주소 : {result.road_address_name}</li>
                  <li>x경도 : {result.x}</li>
                  <li>y위도 : {result.y}</li>
                </ul>
                <hr />
              </>
            );
          })
        : null}
    </Modal>
  );
};

// address_name?: string;
//   category_group_code?: string;
//   category_group_name?: string;
//   category_name?: string;
//   distance?: string;
//   id?: string;
//   phone?: string;
//   place_name?: string;
//   place_url?: string;
//   road_address_name?: string;
//   x?: string;
//   y?: string;

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
