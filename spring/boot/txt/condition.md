## 스프링 부트 @Conditional

`@Conditional`은 스프링 프레임워크에서 제공하는 로우 레벨 어노테이션이고

스프링 부트는 개발자가 다양한 조건문을 작성할 수 있도록 추가적인 `@Conditional` 어노테이션을 제공함

@Conditional이 메타 어노테이션으로 적용되어 있어서, 속성 값으로 지정된 Condition 타입의 메서드 결과에 따라 빈 등록과 기타 어노테이션(@Import 등) 적용 여부를 결정함

#### Class Conditions

`@ConditionalOnClass`: 클래스 패스에 지정한 클래스가 있는 경우 true 반환

`@ConditionalOnMissingClass`: 클래스 패스에 지정한 클래스가 없는 경우 true 반환

#### Bean Conditions

`@ConditionalOnBean`: ApplicationContext에 지정한 타입의 빈이 이미 있는 경우 true 반환

`@ConditionalOnMissingBean`ApplicationContext에 지정한 타입의 빈이 아직 없는 경우 true 반환

#### Property Conditions

`@ConditionalOnProperty`: ApplicationContext에 특정 속성이 설정된 경우 true 반환

#### Resource Conditions

`@ConditionalOnResource`: ApplicationContext에 특정 리소스가 존재하는 경우 true 반환

#### Other Conditions

`@ConditionalOnSingleCandidate`: ApplicationContext에 특정 타입의 빈이 존재하거나, 여러 타입이 있더라도 해당 타입의 primary 빈이 설정된 경우 true 반환