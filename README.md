# 머니로그 (MoneyLog)

개인 수입/지출을 기록하고 카테고리별·월별로 확인하는 가계부 웹 서비스입니다.
멋쟁이사자처럼 백엔드스쿨 캡스톤 프로젝트 (도전 과제 제외, 기본 트랙 기준).

## 문서

- [요구사항 정의서](docs/requirements.md)
- [ERD / 테이블 정의서](docs/erd.md)
- [API 명세서](docs/api-spec.md)

## 실행 방법 (로컬, H2)

IntelliJ에서 프로젝트를 열고 `MoneylogApplication`을 실행하거나, 아래 명령을 사용합니다.

```bash
./gradlew bootRun
```

기본 포트는 8080이고, H2 콘솔은 `/h2-console`에서 확인할 수 있습니다.

## Git 브랜치 전략

- `main`: 항상 동작하는 상태로 유지
- 기능 단위로 `feature/기능이름` 브랜치를 파서 작업 후 `main`에 병합
  - 예: `feature/category-crud`, `feature/jwt-auth`, `feature/ec2-deploy`
- 커밋 메시지 형식: `타입: 요약` (예: `feat: 거래내역 등록 API 추가`)

## 진행 상황

- [x] 1일차: 요구사항/설계 문서, Git 저장소, 프로젝트 셋업
- [ ] 2일차: 카테고리 + 거래내역 CRUD
- [ ] 3일차: JWT 인증/인가 + 통계
- [ ] 4일차: 프론트 연동 + Docker/CI/CD + EC2 배포
- [ ] 5일차: 마무리 (README/회고)
