이번 주차에서는 기존에 사용하던 Spring Data JPA 중심의 구조에서 벗어나, Port Interface를 통해 외부 기술과 핵심 로직의 의존성을 분리하는 방법을 배웠다. 이전에는 Service가 Spring Data JPA의 Repository에 의존하는 방식으로 개발했지만, Repository Port를 Interface로 정의하면서 핵심 로직은 구체적인 저장 기술을 알 필요가 없도록 만들 수 있었다.

이를 통해 실제 저장소가 JPA를 사용하는 DB이든 HashMap을 사용하는 메모리 저장소이든 Port의 구현체로서 사용될 뿐이며, 핵심 로직에는 영향을 주지 않을 수 있다는 것을 이해했다. 또한 이 과정에서 **DIP(의존성 역전 원칙)**를 통해 상위 모듈이 구체적인 외부 기술이 아닌 추상화에 의존하도록 만드는 구조를 배웠다.

추가적으로 외부 프레임워크나 기술에 의존하지 않는 순수 Java 객체인 POJO와 DDD의 Presentation, Application, Domain, Infrastructure 4계층의 역할에 대해서도 학습했다. 이번 실습을 통해 단순히 Controller-Service-Repository로 나누는 것보다 각 계층의 책임과 의존성의 방향을 생각하며 설계하는 것이 중요하다는 것을 알게 되었다.