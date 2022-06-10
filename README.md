# 볼링 게임 점수판
## 진행 방법
* 볼링 게임 점수판 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

---

### 질문 삭제하기 기능 리팩토링

- ### 요구사항
  - 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
  - 로그인 사용자와 질문한 사람이 같은 경우 삭제 가능하다.
  - 답변이 없는 경우 삭제가 가능하다.
  - 질문자와 답변글의 모든 답변자 같은 경우 삭제가 가능하다.
  - 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
  - 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
  - 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory 를 활용해 남긴다.

### 볼링 게임

- ### 요구사항
  - 사용자 1명의 볼링 게임 점수를 관리할 수 있는 프로그램을 구현한다.
  - 각 프레임에 해당하는 점수를 출력하도록 구현한다.
    - 스트라이크: 프레임의 첫번째 투구에서 모든 핀(10개)를 쓰러트린 상태
    - 스페어: 프레임의 두번째 투구에서 모든 핀(10개)를 쓰런트린 상태
    - 미스: 프레임의 두번째 투구에서도 모든 핀이 쓰러지지 않은 상태
    - 거터: 핀을 하나도 쓰러트리지 못한 상태. 커터는 "-" 로 표시
  - 스트라이크는 다음 2번의 투구까지 점수를 합산해야 한다. 스페어는 다음 1번의 투구까지 점수를 합산해야 한다.
  - 10 프레임은 스트라이크이거나 스페어이면 한번 더 투구할 수 있다.

- ### 요구사항 분리
  - 플레이어의 이름은 3글자이여야 한다.
  - 플레이어의 이름은 영어이여야 한다.
  - 두번의 투구를 할 수 있고 투구가 끝나면 다음 프레임으로 넘어간다.
    - 스트라이크경우 바로 다음프레임으로 넘어간다.
    - 마지막 프레임의 경우 스트라이크 혹은 스페어를 할 경우 한번 더 투구를 할 수 있다.
  - 각 프레임마다 10개의 핀 이상 넘길 수 없다.
  - 스트라이크의 경우 10점 + 이후 2번의 투구점수를 합산한다.
    - 마지막 프레임에서 스트라이크를 할 경우 2번의 투구 점수를 합산하지 않는다.
  - 스페어의 경우 10점 + 1번의 투구점수를 합산한다.
    - 마지막 프레임에서 스페어를 할 경우 1번의 투구 점수를 합산하지 않는다.
  - 미스의 경우 첫번째 투구 개수 + 두번째 투구 개수 이다.
  - 현재 프레임의 점수는 프레임이 종료되어야 점수를 계산할 수 있다.
    - 종료 전에 점수를 계산할 시 예외를 반환한다.