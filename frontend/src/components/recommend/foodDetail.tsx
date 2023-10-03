import React, { useState, useEffect } from "react";
import axios from "axios";
import styled, { css } from "styled-components";

import Modal from "react-modal";
import { useRecoilState } from "recoil";
import { foodDetailModal } from "../../recoil/atoms/modalState";

import { Map, MapMarker } from "react-kakao-maps-sdk";

import SearchIcon from "@mui/icons-material/Search";

interface Coords {
  latitude: number | null;
  longitude: number | null;
}

interface Stores {
  address_name?: string;
  category_name?: string;
  phone?: string;
  place_name?: string;
  place_url?: string;
  road_address_name?: string;
}

const FoodDetail = () => {
  // -------------------------- 모달 --------------------------
  const [modal, setModalOpen] = useRecoilState(foodDetailModal);

  // -------------------------- 현위치 -------------------------
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

  // -------------------------- 검색 --------------------------
  let foodName = "짜장면";
  const [info, setInfo] = useState<any>(); // info 상태의 타입은 여러 가지 일 수 있으므로 any를 사용합니다.
  const [markers, setMarkers] = useState<any[]>([]); // markers 상태의 타입은 여러 가지 객체 배열일 수 있으므로 any[]를 사용합니다.
  const [map, setMap] = useState<any>(); // map 상태의 타입은 여러 가지 일 수 있으므로 any를 사용합니다.

  const mapLevel = [50, 100, 250, 500, 1000]; // 지도 레벨

  const ps = new kakao.maps.services.Places(); // 위치 기반 검색
  const [stores, setStores] = useState<Stores[]>([]); // 검색 결과

  const [clicked, setClicked] = useState(false); // 재검색 버튼

  // ---------------------------------------------------------

  // 최초 1회 검색 및 지도 생성
  useEffect(() => {
    if (!map) return;

    setMarkers([]);
    setStores([]);

    map.setMinLevel(3); // 맵 최소레벨
    map.setMaxLevel(7); // 맵 최대레발

    ps.keywordSearch(foodName, placesSearchCB, {
      category_group_code: "FD6",
      x: Number(`${coords.longitude}`),
      y: Number(`${coords.latitude}`),
      radius: 500,
      sort: kakao.maps.services.SortBy.DISTANCE,
    });
  }, [map]);

  // 장소검색이 완료됐을 때 호출되는 콜백함수
  function placesSearchCB(data: any, status: any, pagination: any) {
    if (!map) return;
    if (status === kakao.maps.services.Status.OK) {
      // 검색 결과 저장
      setStores(data);

      // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
      // LatLngBounds 객체에 좌표를 추가합니다
      // const bounds = new kakao.maps.LatLngBounds();
      const newMarkers: any[] = [];

      for (let i = 0; i < data.length; i++) {
        newMarkers.push({
          position: {
            lat: data[i].y,
            lng: data[i].x,
          },
          content: data[i].place_name,
        });
        // bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
      }
      setMarkers(newMarkers);

      // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
      // map.setBounds(bounds);
    }
  }

  // 버튼을 눌러서 지도 중심으로 재검색
  const search = () => {
    console.log("음식점 검색");

    // 현재 맵 중심 좌표
    let latlng = map.getCenter();

    // info창 지우기
    setInfo(null);

    ps.keywordSearch(foodName, placesSearchCB, {
      category_group_code: "FD6",
      x: latlng.getLng(),
      y: latlng.getLat(),
      radius: mapLevel[map.getLevel() - 3] * 2,
      sort: kakao.maps.services.SortBy.DISTANCE,
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

      <SearchButtonLayout>
        <SearchButtonStyle
          onClick={search}
          clicked={clicked}
          onTouchStart={() => {
            setClicked(true);
          }}
          onTouchEnd={() => {
            setClicked(false);
          }}
        >
          <SearchIcon fontSize="small" style={{ transform: "translate(-5%, 25%)" }} />
          <p style={{ display: "inline-block" }}>현 지도에서 검색</p>
        </SearchButtonStyle>
      </SearchButtonLayout>
      <Map // 로드뷰를 표시할 Container
        center={{
          lat: Number(`${coords.latitude}`),
          lng: Number(`${coords.longitude}`),
        }}
        style={{
          width: "100%",
          height: "25vh",
        }}
        level={7}
        onCreate={setMap}
      >
        <MapMarker
          position={{ lat: Number(coords.latitude), lng: Number(coords.longitude) }}
          image={{
            src: "/images/현위치 마커.png", // 마커이미지의 주소입니다
            size: {
              width: 50,
              height: 50,
            }, // 마커이미지의 크기입니다
          }}
        />

        {markers.map((marker: any) => (
          <MapMarker
            key={`marker-${marker.content}-${marker.position.lat},${marker.position.lng}`}
            position={marker.position}
            onClick={() => setInfo(marker)}
          >
            {info && info.content === marker.content && (
              <div style={{ color: "#000" }}>{marker.content}</div>
            )}
          </MapMarker>
        ))}
      </Map>

      <hr />

      {stores
        ? stores.map((result, idx) => {
            return (
              <>
                <ul>
                  <li>주소이름 : {result.address_name}</li>
                  <li>카테고리 : {result.category_name}</li>
                  <li>전화번호 : {result.phone}</li>
                  <li>장소명 : {result.place_name}</li>
                  {/* <li>url : {result.place_url}</li> */}
                  <li>주소 : {result.road_address_name}</li>
                </ul>
                <hr />
              </>
            );
          })
        : null}
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

// CSS

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
    width: "90vw",
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

const SearchButtonLayout = styled.div`
  // layout
  position: absolute;
  top: 12%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
`;
const SearchButtonStyle = styled.button<{ clicked: boolean }>`
  display: inline-block;
  padding: 0.5vmin 1rem 0.5rem 0.5rem;
  /* width: 40vmin; */
  /* height: 7vmin; */
  border-radius: 1rem;
  background-color: ${(props) => (props.clicked ? "whitegray" : "white")};
  border: 0px;
  box-shadow: 0 0 0.5rem rgba(0, 0, 0, 0.4);
  /* font-size: 0.8rem; */
  color: #fe9d3a;
  font-weight: bold;
`;
