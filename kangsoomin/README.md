# 1 ) 2023-09-06 (수)

프로젝트 아이디어 회의를 위해 와이어 프레임보다 더 간단한 그림을 그려가며 기능 명세를 진행했다. 기능이 바뀔 때 마다 그림을 다시 그려야 하면 헛수고가 되는게 아닌가 했지만, 소통에 있어서 훨씬 더 편해지고 좋았던 것 같다.
내일부터는 빅데이터 알고리즘을 적용하기 위한 파라미터들을 명세하며 요구사항 명세까지 진행해 볼 생각이다.
금요일까지는 ERD 가안이 나오도록 준비할 예정이다.

# 2 ) 2023-09-07 (목)

프로젝트의 추천 알고리즘 부분이 어려워서 그런지 지금까지 논의가 덜 되고 뒤로 미러두게 된 것 같았다. 우리 프로젝트는 추천 알고리즘이 핵심이고 컨설턴트님과 전문가 리뷰에서 검사 받기 위해서는 보다 정확하게 우리 알고리즘을 정의할 필요가 있어 보인다.
추천 알고리즘의 학습과 메뉴 레시피 데이터와 같이 확실히 필요한 데이터의 확보를 우선적으로 진행할 예정이다.

FE담당으로써 React 기본 학습은 이번 주 내로 마무리할 예정이다.

# 3 ) 2023-09-08 (금)

추천 알고리즘으로 통계적 알고리즘과 딥러닝 기반 알고리즘이 있다는 것을 구분하게 되었다. 우리는 딥러닝 기반을 사용해 볼 생각으로 학습을 선행해볼 생각이다.
음식 선택에 있어서 구분해야하는 파라미터를 명확히해야겠다.

# 4 ) 2023-09-11 (월)

본격적인 개발이 시작되는 기간이지만, 우리 팀은 디자인과 알고리즘 명세 부분이 덜 되서 그 부분의 논의를 진행했다.

알고리즘 부분은 계속 빙빙 도는 느낌이어서 내가 데이터 플로우를 시각적으로 볼 수 있게 그려서 도움을 주려 했지만,
그냥 백엔드 몇 명이 담당해서 맡는게 더 효과적인 것 같다.

그래서 프론트는 와이어 프레임과 목업 디자인을 서둘러 진행하기로 했고,
백엔드는 추천 알고리즘 명세, ERD 제작, CI/CD로 나누어서 진행하기로 했다.

컨설팅을 받았는데, 보여드릴 게 데이터 플로우 그린 것과 임시 ERD 뿐이어서 보여드렸지만, 우리팀은 늦어지기도 했고
빨리 개발을 진행해야 할 것 같다고 하셨다.

논의는 어느정도 마무리하고 분업 및 간트 차트 같은 일정 관리가 필요 할 것 같다.
데드라인을 어느정도 정해 놓을 생각이다.

# 5 ) 2023-09-12 (화)

- 진행
  - 디자인 레퍼런스 조사
  - 와이어 프레임(미완)
  - 구글 피트니스 활동 데이터
- 느낀점
  - 유명한 '네카라쿠배당' 중 라인을 제외하고는 UX/UI 디자인 참고를 위해 서비스를 켑쳐해 와이어 프레임 피그마 하단에 기록했다. 확실히 보면서 배우게 되는 것들이 많은 것 같다.
  - 와이어 프레임은 기능 명세서에 명시된 기능들이 일단 다 들어가는게 일차적인 목표이다. 위치나 전환되서 나올 페이지 종류만 나열하고 디자인 부분은 목업에서 좀더 명확하게 만들어볼 생각이다.
  - 구글 피트니스 데이터를 가져오는데 성공했다. 이렇게 삼성 헬스나 기타 피트니스 앱과 연동해서 활동 데이터를 얻을 수 있게 되었다. 메뉴 추천의 좋은 근거를 하나 더 얻게 되었다.

# 6 ) 2023-09-13 (수)

- 진행
  - 와이어 프레임 작성
  - ERD 피드백
- 느낀점
  - 와이어 프레임을 거의 완성했는데, 확실히 디자인을 그리고 나서야 상세한 요구사항이 명시되는 것 같다. 원래 반대로 진행하는 것이지만, 요구사항을 직접 도출하며 구멍이 많아지는 건 어쩔 수 없는 것 같다.
  - ERD도 어느덧 마무리가 된 것 같아서 이제 이번 주는 디자인과 데이터 전처리, 프로젝트 초기 환경 세팅과 CI/CD로 진행 될 것 같다. 드디어 길고 긴 아이디어 정리가 마무리 되고 코드 구현을 앞두고 있어서 설레기도하고 걱정도 된다.
  - React와 UI/UX를 맡은 바 열심히 알아보고 공부해서 좋은 결과물이 나오도록 힘써야겠다.

# 7 ) 2023-09-14 (목)

- 진행
  - 중간 발표 ppt 작성
  - 목업 작성(미완)
- 느낀점
  - 발표 자료를 준비하면서, 확실히 우리 추천 시스템이 설명하기 어렵다는걸 느꼈다. 쉽고 간단하게 설명하기 위해서 노력하다보니 더 잘 이해하게 된 것 같다.
  - CI/CD가 마무리 되면 바로 프론트 개발을 들어가야하는데, 개발에 지장이 없도록 철저히 학습해놔야겠다.
  - 목업 디자인이 진행 중인데 우리 프로젝트는 UX가 아주 중요하다고 생각한다. 좋은 디자인을 만들 수 있도록 많은 고민과 노력을 쏟을 생각이다.

# 8 ) 2023-09-15 (금)

- 진행
  - 중간 발표
  - 주말 진행 계획
- 느낀점
  - 중간 발표에서 우리 팀의 주제를 그래도 잘 전달한 것 같다. 하지만 기술 스택이나 아키텍쳐가 명시되지 않았고, 추천 알고리즘에 대한 설명도 부족했던 것 같아서 최종 발표에서 추가할 계획이다.
  - 주말 동안 산출물을 마무리하고, 다음 주는 최대한 코드 구현과 데이터 전처리를 병행하며 개발 파트를 진행 할 계획이다.
  - 비동기 소통을 위해서 저녁마다 개인 과제 검사를 약속했다. 이번 주가 잘 진행 되는지 보고 계속 진행 할 지 정해야겠다.

# 9 ) 2023-09-18 (월)

- 진행
  - 로그인 페이지 디자인
  - 친구 리스트 페이지 디자인
- 느낀점
  - 피그마 디자인을 컴포넌트를 나눠서 하다보니 초반엔 시간이 많이 걸리게 되는 것 같다.
  - 이제 요령이 점점 더 많이 붙어서 속도도 올라가고 있으니 꾸준하게 디자인을 완료하자.
  - 디자인은 수요일까지 마무리 할 예정이다. 지금 같은 속도면 가능도 하겠지만, 더 이상 밀리면 안되는 일정이기 때문에 초조함도 있다.
  - 그와 동시에 리액트 강의도 듣고 실습도 해야하고, 타입스크립트나 동우님이 쓰자고 하는 다양한 라이브러리도 학습을 꾸준히 해야한다. 힘내자.

# 10 ) 2023-09-19 (화)

- 진행
  - 추천 페이지 디자인
- 느낀점
  - 메인 기능을 디자인 하는 것이라 그런지 와이어프레임으로 기본 골자를 디자인 해놨음에도 불구하고 오래걸렸다.
  - 더군다나 UI/UX가 가장 중요한 부분이라 피드백도 많이 받다보니 자잘한 수정 사항이 많이 생기는 것 같다.
  - 공통 때는 바꿔야 하는 부분을 지적하는 입장이었는데, 디자인 파트를 맡으니 피드백을 받고 수정사항이 생기는게 부담으로 다가왔다. 이제야 프론트 입장을 이해 할 수 있게 된 것 같다. 하지만 더 나은 디자인을 위해 계속 피드백을 받아 볼 생각이다.

# 11 ) 2023-09-20 (수)

- 진행
  - 그룹 페이지 디자인
  - 회원 가입 페이지 디자인(미완)
- 느낀점
  - 큰 산으로 생각했던 그룹 페이지의 디자인을 어느 정도 마무리했다. 부담을 많이 느꼈는데, 기능을 심플하게 가져가자고 생각하니 금방 그려낼 수 있던 것 같다. 중간중간 사소한 고민들은 팀원들에게 의견을 받으니 더욱 효율적으로 만들 수 있었다. 역시 피드백을 적극 활용해야 디자인이 잘 된다.
  - 동우님의 원래 몫이었던 메인 페이지와 마이 페이지가 디자인이 너무 미흡해서 오늘 저녁에 내가 수정하기로 했다. 맡은 파트는 데드라인 내에 끝내서 뿌듯하면서도, 일정 부분 도맡아서 해야하니 스트레스 받기도 하는 것 같다.
  - 그래도 프로젝트 퀄리티를 위해 열심히 디자인을 마무리할 생각이다.
  - 내일은 양희제 코치님이 피드백도 해주시기로 했으니, 피드백을 반영 한 뒤 디자인을 마무리하고 API명세를 진행하면 될 것 같다.