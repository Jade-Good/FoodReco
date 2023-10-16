<img src="https://capsule-render.vercel.app/api?type=waving&color=FE9D3E&height=250&section=header&text=FoodReco&fontSize=90&fontAlign=70&fontAlignY=40&fontColor=FFE7D1" />

### 목차

| [&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;소개&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;](#소개) | [&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;구현&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;](#구현) | [&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;마치며&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;](#마치며) |
| :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
|                                                                                                    [🎈 개요](#🎈-개요)                                                                                                    |                                                                                                    [🚀 기능](#🚀-기능)                                                                                                    |                                                                                                      [👦 팀원](#👦-팀원)                                                                                                      |
|                                                                                      [✨ 프로젝트 기획 배경](#✨-프로젝트-기획-배경)                                                                                      |                                                                                           [🍕 빅데이터 추천](#🍕-빅데이터-추천)                                                                                           |                                                                                                      [📣 소감](#📣-소감)                                                                                                      |
|                                                                                               [🌳 빌드 환경](#🌳-빌드-환경)                                                                                               |                                                                                                  [👀 산출물](#👀-산출물)                                                                                                  |                                                                                                 [🌱 회고 기록](#🌱-회고-기록)                                                                                                 |
|                                                                                             [🎯 기술 차별점](#🎯-기술-차별점)                                                                                             |                                                                                             [📚 파일 구조도](#📚-파일-구조도)                                                                                             |                                                                                                                                                                                                                               |

<br/>
<br/>

# 소개

## 🎈 개요

**_🖐 SSAFY 9기 2학기 특화 프로젝트 🖐_**  
빅데이터 추천 알고리즘을 사용한 서비스를 개발해보자

> 2023.08.25 ~ 2023.10.06 (6주)

[🔼 목차로 돌아가기](#목차)

<br/>

## ✨ 프로젝트 기획 배경

**"오늘 저녁 뭐먹지?"**

> 매일 사람들이 고민하는 이 고민을 _'빅데이터 추천 알고리즘'_ 을 이용해서 해결해보고자 했습니다.  
> 우리는 주위 친구, 동료들에게 묻곤 하지만, 나의 기분과 경제상황, 최근에 먹은 음식들을 다 고려해서 추천해서 추천하기란 쉽지 않아 금새 실증이 나곤 합니다.

<br/>

**"알아서 내 상황을 고려해 추천해줄 수는 없을까?"**

> 나의 음식 취향, 최근 먹은 메뉴, 오늘 활동량, 알러지, 날씨 등 다양한 요소들을 모두 고려한 하이 태크 추천 시스템을 만들어보고자 했습니다.  
> 서버의 숨겨진 뒷 작업으로는 많은 데이터가 오가지만, 사용자가 보기에는 딱히 귀찮게 입력하는 것 없이 오늘의 메뉴가 추천되는 컨셉입니다.

[🔼 목차로 돌아가기](#목차)

<br/>

## 🌳 빌드 환경

| FrontEnd                    | BackEnd                        | Database               | Infra                      |
| :-------------------------- | :----------------------------- | :--------------------- | :------------------------- |
| Node.js 16.18.50            | Java 11 (Oracle jdk : 11.0.20) | MySQL 8.0.33           | AWS EC2 (Ubuntu 20.04 LTS) |
| React.js 18.2.21            | Spring Boot 2.7.15             | MySQL Workbench 8.0 CE | AWS S3 Bucket              |
| react-router-dom 6.14.2     | Gradle 8.2.1                   | Redis 7.2.1            | Nginx 1.18.0               |
| react-toastify 9.1.3        | Spring Security 5.7.10         |                        | Jenkins 2.402              |
| react-hook-form 7.46.1      | JPA & QueryDsl                 |                        | Docker 23.0.6              |
| react-hot-toast 2.4.1       | Hibernate 7.0.5                |                        | Docker Compose 2.17.3      |
| react-icons 4.11.0          | IntelliJ Ultimate 2023.1.3     |                        | SonarQube 10.0.0.68432     |
| react-kakao-maps-sdk 1.1.21 | JWT 4.2.1                      |
| react-modal 3.16.1          | smtp 2.7.0                     |
| react-query 3.39.3          | S3 2.2.1                       |
| Axios 1.5.0                 | lombok 1.18.14                 |
| TypeScript 4.9.5            |                                |
| Recoil 0.7.7                |                                |
| Styled-Components 6.0.8     |                                |
| mui-iconsmaterial 5.14.11   |                                |
| mui-material 5.14.11        |                                |

[🔼 목차로 돌아가기](#목차)

<br/>

## 🎯 기술 차별점

[🔼 목차로 돌아가기](#목차)

<br/>

# 구현

## 🚀 기능

|                                                             로그인                                                             |                                           회원가입                                            |             마이페이지              |
| :----------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------: | :---------------------------------: |
|                                                  ![로그인](./img/로그인.gif)                                                   |                                ![회원가입](./img/회원가입.gif)                                | ![마이페이지](./img/마이페이지.gif) |
| 로그인입니다. <br> 로그인 기능을 설명해야하는데 <br> 주절주절 떠들 면 표가 어떻게 되려나?? <br> 좀 길어질텐데 어케될까요? <br> | 회원가입이다. <br> 얘도 주절주절 떠들면 어떻게될까? <br> 설명을 여기다 넣는다는게 좀 에바였나 |             마이페이지              |

|            메뉴추천             |          친구           |           그룹           |
| :-----------------------------: | :---------------------: | :----------------------: |
| ![메뉴추천](./img/메뉴추천.gif) | ![친구](./img/친구.gif) | !![그룹](./img/그룹.gif) |
|          메뉴추천이다           |       친구추가다        |         그룹기능         |

[🔼 목차로 돌아가기](#목차)

<br/>

## 🍕 빅데이터 추천

[🔼 목차로 돌아가기](#목차)

<br/>

## 👀 산출물

### 와이어 프레임, 디자인, 목업

### ERD

![ERD](./img/특화ERD.png)

### 시스템 구조도

![시스템구조도](./img/시스템구조도.png)

### 간트차트

<img src="./img/간트차트1.png" width=40%>
<img src="./img/간트차트2.png" width=40%>

### 지라 이슈

![지라이슈](./img/지라이슈.png)

### 지라 번다운 차트

|            1주차 스프린트             |            2주차 스프린트             |            3주차 스프린트             |
| :-----------------------------------: | :-----------------------------------: | :-----------------------------------: |
| ![번다운차트1](./img/번다운차트1.png) | ![번다운차트2](./img/번다운차트2.png) | ![번다운차트3](./img/번다운차트3.png) |

|            4주차 스프린트             |            5주차 스프린트             |
| :-----------------------------------: | :-----------------------------------: |
| ![번다운차트4](./img/번다운차트4.png) | ![번다운차트5](./img/번다운차트5.png) |

### 중간발표 ppt

![중간발표](./img/중간발표.gif)

### 최종발표 ppt

![최종발표](./img/최종발표.gif)
[🔼 목차로 돌아가기](#목차)

<br/>

## 📚 파일 구조도

### FrontEnd

```
📦frontend
 ┣ 📂public
 ┃ ┣ 📂images
 ┣ 📂src
 ┃ ┣ 📂components
 ┃ ┃ ┣ 📂crewpage
 ┃ ┃ ┣ 📂footer
 ┃ ┃ ┣ 📂friend
 ┃ ┃ ┣ 📂header
 ┃ ┃ ┣ 📂inputs
 ┃ ┃ ┣ 📂membercomponents
 ┃ ┃ ┣ 📂option
 ┃ ┃ ┗ 📂recommend
 ┃ ┣ 📂fonts
 ┃ ┣ 📂hooks
 ┃ ┣ 📂model
 ┃ ┣ 📂pages
 ┃ ┃ ┣ 📂crew
 ┃ ┃ ┣ 📂crewRecomendation
 ┃ ┃ ┣ 📂friend
 ┃ ┃ ┣ 📂login
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┣ 📂member
 ┃ ┃ ┣ 📂memberRecommendation
 ┃ ┃ ┣ 📂notfound
 ┃ ┃ ┣ 📂singup
 ┃ ┣ 📂recoil
 ┃ ┃ ┗ 📂atoms
 ┃ ┣ 📂redux
 ┃ ┃ ┣ 📂slices
 ┃ ┃ ┗ 📂store
 ┃ ┣ 📂styles
 ┃ ┣ 📂utils
```

### BackEnd

```
📦backend
 ┣ 📂src
 ┃ ┗ 📂main
 ┃ ┃ ┗ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂ssafy
 ┃ ┃ ┃ ┃ ┃ ┗ 📂special
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂crew
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂etc
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂food
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂crew
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂etc
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂food
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂crew
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂etc
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂food
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂util

 📦fastapi
 ┣ 📂app
 ┃ ┣ 📂model

 📦dataproccess
 ┣ 📂food_process
 ┃ ┣ 📂model
 ┃ ┣ 📂preprocessed_data
 ┃ ┃ ┣ 📂food_calorie
 ┃ ┃ ┗ 📂menu_ingredient
```

[🔼 목차로 돌아가기](#목차)

<br/>

# 마치며

## 👦 팀원

| [강수민(팀장)](https://github.com/Jade-Good) |     [김동우](#👦-팀원)      | [이대건](https://github.com/leedaegeon) | [임규돈](https://github.com/KyuDonLim) | [조준희](https://github.com/jjunehee) |
| :------------------------------------------: | :-------------------------: | :-------------------------------------: | :------------------------------------: | :-----------------------------------: |
|         ![강수민](./img/강수민.png)          | ![김동우](./img/김동우.png) |       ![이대건](./img/이대건.png)       |      ![임규돈](./img/임규돈.png)       |      ![조준희](./img/조준희.png)      |
|                   FE / PM                    |             FE              |               BE / RecSys               |                 BE /DB                 |              BE / Infra               |

### 팀원 역할

**_FrontEnd_**

- 강수민
  - 디자인
  - 컴포넌트
- 김동우

**_BackEnd_**

- 이대건
- 임규돈
- 조준희

**_Infra_**

- 조준희

[🔼 목차로 돌아가기](#목차)

<br/>

## 📣 소감

[🔼 목차로 돌아가기](#목차)

<br/>

## 🌱 회고 기록

### 데일리 회고

[🔹 강수민 데일리 회고](./회고/kangsoomin/README.md)

[🔹 김동우 데일리 회고](./회고/kimdongwoo/Readme.md)

[🔹 이대건 데일리 회고](./회고/leedaegeon/Readme.md)

[🔹 임규돈 데일리 회고](./회고/limkyudon/README.md)

[🔹 조준희 데일리 회고](./회고/chojunhee/README.md)

[🔼 목차로 돌아가기](#목차)

<br/>

### 스크럼

### 스프린트 회고
