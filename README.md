# SAFFY 특화 프로젝트: Food Reco

<div align="center">
  <img src="./img/food_reco_logo.png" width="125" />
</div>

## 목차

| No. | 구현 기능                                         |
| --- | ------------------------------------------------- |
| 1   | [Food Reco란?](#1-food-reco란)                    |
| 2   | [기존 메뉴 추천 시스템](#2-기존-메뉴-추천-시스템) |
| 3   | [Food Reco 구현 사항](#3-food-reco-구현-사항)     |
| 4   | [빅데이터 전처리](#4-빅데이터-전처리)             |
| 5   | [개발 기간](#5-개발-기간)                         |
| 6   | [팀원](#6-팀원)                                   |
| 7   | [담당 역할](#7-담당-역할)                         |
| 8   | [개발 환경](#8-개발환경)                          |
| 9   | [지라 번다운 차트](#9-지라-번다운-차트)           |
| 10  | [깃 플로우](#10-깃-플로우)                        |
| 11  | [프로젝트 리뷰](#11-프로젝트-리뷰)                |

## 1) Food Reco란?

- 사용자의 취향에 맞춘 메뉴 추천 시스템입니다.

## 2) 기존 메뉴 추천 시스템

- 개인이 먹을 음식만을 추천하여 여럿이서 먹을 때 도움을 주지 못합니다.

## 3) Food Reco 구현 사항

- 마이페이지 및 친구, 개인 음식 추천

<div align="center">
<img src="./img/mypage.jpg" width="125" height="250"/>
  <img src="./img/friend_list.jpg" width="125" height="250"/>
  <img src="./img/personal_reco.PNG" width="125" height="250"/>
</div>

- 그룹 참가 및 그룹 상세페이지, 투표 페이지

<div align="center" style="margin-bottom: 30px">
  <img src="./img/group_list.jpg" width="125" height="250"/>
    <img src="./img/group_detail.jpg" width="125" height="250"/>
    <img src="./img/group_vote.jpg" width="125" height="250"/>

</div>

- 구글 피트니스 연동을 통해 개인의 활동량 데이터를 받습니다.
- 개인 음식 피드백 점수, 날씨, 활동량, 재료를 기반으로 사전에 학습시킨 TF-IDF 벡터의 코사인 유사도를 비교하여 음식을 추천합니다.
- 추천 알고리즘 결과중 특정 음식이 알러지 재료를 포함하거나, 차단 또는 최근에 먹은 음식이 있다면 필터링 하여 제공합니다.
- 그룹 음식 추천은 모든 그룹원의 개인 추천 결과를 합집합 한 뒤 내부적인 점수를 기준으로 추천합니다.
- 그룹원은 투표를 통해 함께 먹을 음식을 정할 수 있습니다.

## 4) 빅데이터 전처리

1. 만개의 레시피 데이터중 음식명 열에서 음식명이 아닌 음식 제거
2. 중복되는 음식명 제거
3. 재료가 카테고리형식으로 되어있지 않았기에 재료를 30개로 재한하여 정리
4. 재료에는 없지만 음식명에 재료가 포함되어 있는 경우 재료에 추가
5. 조리방식, 음식분류1, 음식분류2 열을 통일시켜 정리
6. TF-IDF 벡터 유사도를 사용하여 음식명이 유사한 행을 한개만 남기고 제거

## 5) 개발 기간

- 2023년 8월 25일 ~ 2023년 10월 6일

## 6) 팀원

- 강수민(팀장)
- 김동우
- 조준희
- 임규돈
- 이대건

### 7) 담당 역할

- 이대건: 빅데이터 전처리 및 백엔드 개발

  - 음식 빅데이터 전처리
  - fastAPI 추천 시스템 개발
  - spring boot와 fastAPI 통신
  - spring 서버에서 추천 결과 후처리(필터링)
  - 구글 OAuth2.0 권한 연동
  - google fitness API를 통해 유저 걸음 정보 수집

## 8) 개발환경

### 백엔드

![Static Badge](https://img.shields.io/badge/java-11-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/Spring%20Boot-2.7.15-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/redis-2.7.15-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/JPA-5.6.15-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/Spring%20Security-5.7.10-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/Webflux-2.7.15-0B9A47)

![Static Badge](https://img.shields.io/badge/Python-3.11-0B9A47)
![Static Badge](https://img.shields.io/badge/fastAPI-0.103.1-0B9A47)

### 프론트 엔드

![Static Badge](https://img.shields.io/badge/Node-18.16.1-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/React-18.2.0-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/Axios-1.5.0-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/React%20router%20dom-6.14.2-0B9A47?style=flat-square)
![Static Badge](https://img.shields.io/badge/recoil-0.7.7-0B9A47)

## 9) 지라 번다운 차트

![img](./img/food_reco_burndown.PNG)

## 10) 깃 플로우

- master: 실제 서비스에 적용될 코드
- develop(default) : 스프린트 단위로 적용될 코드(1주)
- feat : 맡은 파트별 기준 브랜치(띄어쓰기는 -)
  - `feature/fe/login`
  - `feature/be/login-api`
- hotfix: master에서 급하게 수정할 것

## 11) 프로젝트 리뷰

- 이대건
  - 내부적으로는 많은 데이터를 처리하여 결과를 도출하지만 발표 시에 처리 과정을 드러내지 못해 아쉬움이 남습니다.
  - 데이터 전처리를 일일이 해야 하는 것인 것을 깨달았고 앞으로도 데이터 전처리를 하게 되면 더 꼼꼼히 하고 싶습니다.
