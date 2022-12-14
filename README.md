# 📚 Re-Mate
Re-Mate는 가상 환경 내에서 함께 독서 모임을 진행하고, 독서 기록을 공유하는 메타버스 기반 독서 플랫폼입니다.


![re-mate](https://user-images.githubusercontent.com/85207194/207511293-c11478ae-7cdf-4536-a6a2-70d5d0d6f6b5.jpg)

## 🎆 프로젝트 기획
 문화체육관광부의 2021 독서실태조사에 따르면, 2013년 이후 연간 종이책 독서율은 꾸준히 감소하고 있는 반면,
전자책 독서율은 꾸준히 성장하고 있는 것을 확인할 수 있습니다. 이는 독서의 방식이 오프라인에서 온라인으로 변화하고 있음을 나타내는 지표로, 
고전적인 독서의 방식이 변화하고 있음을 보여줍니다.

이러한 독서 방식의 전환은, e-book을 중심으로 한 온라인에서의 독서 활동과 
독서 관련 온라인 서비스들의 성장을 촉진시켰습니다.

이러한 배경 아래에서, Re-Mate는 메타버스라는 가상 환경을 중심으로 독서를 기록하고 공유할 수 있는
환경을 제공함으로써 온라인 독서 플랫폼의 새로운 방향을 제시하고자 합니다.


## 💡 INFO
Re-Mate 프로젝트는 [MileStone](https://github.com/TheHabit/the-habit-spring-server/milestones), [Issue](https://github.com/TheHabit/the-habit-spring-server/issues)를 활용하여 이슈 관리를 진행했습니다. 자세한 사항은 링크를 통해 확인할 수 있습니다.

## 📝 프로젝트 목표
1. Unity 클라이언트와 AI 서버와의 협업
2. AWS를 활용한 클라우드 아키텍처 설계 및 배포

## ✅ 기술스택
![기술스택](https://user-images.githubusercontent.com/75306582/204977701-270a5947-1ae3-4939-b96f-319ba97586e5.jpg)

## ✅ 시스템 아키텍처 
![ReMate Architect](https://user-images.githubusercontent.com/75306582/204968887-27244572-da7e-4883-9c90-4471326b36fd.png)

## ✅ 브랜치 전략
### Github Flow 전략 활용
![githubflow](https://user-images.githubusercontent.com/85207194/204445038-50e832b9-7440-47a9-9778-8b17d1c616c5.png)
> 이미지 출처: https://quangnguyennd.medium.com/git-flow-vs-github-flow-620c922b2cbd#:~:text=Unlike%20Git%2DFlow%2C%20GitHub%2D,processing%20methods%20should%20be%20similar. 
+ 작업 의도를 명확히 파악할 수 있도록, 브랜치의 프리픽스 (feat, fix 등)를 명확히 적도록 함
+ 자세한 commit 메세지와 주기적인 push를 통한 작업 공유
+ 코드 리뷰를 거치는 pull request
+ Github Actions을 통한 CI/CD  
[PR 내역 확인하기](https://github.com/TheHabit/the-habit-spring-server/pulls?q=is%3Apr+is%3Aclosed)

## ✅ ERD
+ 회원, 독서 기록, 독서 모임을 포함한 ERD

![thehabbit-spring-server](https://user-images.githubusercontent.com/85207194/204455530-f452e0da-f612-4269-8d17-0a3771024d45.png)
