Spring Boot 게시판 프로젝트

프로젝트 구조
├─.gradle
│  ├─8.7
│  │  ├─checksums
│  │  ├─dependencies-accessors
│  │  ├─executionHistory
│  │  ├─expanded
│  │  ├─fileChanges
│  │  ├─fileHashes
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  └─vcs-1
├─.idea
│  └─modules
├─build
│  ├─classes
│  │  └─java
│  │      ├─main
│  │      │  └─com
│  │      │      └─example
│  │      │          └─forum
│  │      │              ├─admin
│  │      │              │  ├─controller
│  │      │              │  └─dto
│  │      │              │      ├─requests
│  │      │              │      └─response
│  │      │              ├─base
│  │      │              │  ├─auth
│  │      │              │  │  └─service
│  │      │              │  ├─board
│  │      │              │  │  └─service
│  │      │              │  ├─comment
│  │      │              │  │  ├─dto
│  │      │              │  │  │  ├─request
│  │      │              │  │  │  └─response
│  │      │              │  │  └─service
│  │      │              │  ├─config
│  │      │              │  └─image
│  │      │              │      └─service
│  │      │              ├─boards
│  │      │              │  ├─freeBoard
│  │      │              │  │  ├─board
│  │      │              │  │  │  ├─controller
│  │      │              │  │  │  ├─dto
│  │      │              │  │  │  │  ├─request
│  │      │              │  │  │  │  └─response
│  │      │              │  │  │  ├─entity
│  │      │              │  │  │  ├─repository
│  │      │              │  │  │  └─service
│  │      │              │  │  ├─comment
│  │      │              │  │  │  ├─controller
│  │      │              │  │  │  ├─entity
│  │      │              │  │  │  ├─repository
│  │      │              │  │  │  └─service
│  │      │              │  │  └─image
│  │      │              │  │      ├─entity
│  │      │              │  │      ├─repository
│  │      │              │  │      └─service
│  │      │              │  ├─imageBoard
│  │      │              │  │  └─controller
│  │      │              │  └─questionBoard
│  │      │              │      ├─board
│  │      │              │      │  ├─controller
│  │      │              │      │  ├─dto
│  │      │              │      │  │  ├─request
│  │      │              │      │  │  └─response
│  │      │              │      │  ├─entity
│  │      │              │      │  ├─repository
│  │      │              │      │  └─service
│  │      │              │      ├─comment
│  │      │              │      │  ├─controller
│  │      │              │      │  ├─entity
│  │      │              │      │  ├─repository
│  │      │              │      │  └─service
│  │      │              │      └─image
│  │      │              │          ├─entity
│  │      │              │          ├─repository
│  │      │              │          └─service
│  │      │              ├─main
│  │      │              └─user
│  │      │                  ├─controller
│  │      │                  ├─dto
│  │      │                  │  ├─requests
│  │      │                  │  └─response
│  │      │                  ├─entity
│  │      │                  ├─repository
│  │      │                  └─service
│  │      └─test
│  │          ├─com
│  │          │  └─example
│  │          │      └─forum
│  │          └─generated_tests
│  ├─generated
│  │  └─sources
│  │      ├─annotationProcessor
│  │      │  └─java
│  │      │      ├─main
│  │      │      └─test
│  │      └─headers
│  │          └─java
│  │              ├─main
│  │              └─test
│  ├─reports
│  │  └─tests
│  │      └─test
│  │          ├─classes
│  │          ├─css
│  │          ├─js
│  │          └─packages
│  ├─resources
│  │  └─main
│  │      ├─static
│  │      │  ├─account
│  │      │  ├─logo
│  │      │  ├─mobile
│  │      │  └─navbar
│  │      └─templates
│  │          ├─admin
│  │          ├─boards
│  │          │  ├─freeBoard
│  │          │  ├─imageBoard
│  │          │  └─questionBoard
│  │          ├─layouts
│  │          ├─main
│  │          ├─message
│  │          └─user
│  │              └─mypage
│  ├─test-results
│  │  └─test
│  │      └─binary
│  └─tmp
│      ├─compileJava
│      │  └─compileTransaction
│      │      ├─backup-dir
│      │      └─stash-dir
│      ├─compileTestJava
│      │  └─compileTransaction
│      │      ├─backup-dir
│      │      └─stash-dir
│      └─test
├─gradle
│  └─wrapper
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─forum
    │  │              ├─admin
    │  │              │  ├─controller
    │  │              │  └─dto
    │  │              │      ├─requests
    │  │              │      └─response
    │  │              ├─base
    │  │              │  ├─auth
    │  │              │  │  └─service
    │  │              │  ├─board
    │  │              │  │  └─service
    │  │              │  ├─comment
    │  │              │  │  ├─dto
    │  │              │  │  │  ├─request
    │  │              │  │  │  └─response
    │  │              │  │  └─service
    │  │              │  ├─config
    │  │              │  └─image
    │  │              │      ├─controller
    │  │              │      └─service
    │  │              ├─boards
    │  │              │  ├─freeBoard
    │  │              │  │  ├─board
    │  │              │  │  │  ├─controller
    │  │              │  │  │  ├─dto
    │  │              │  │  │  │  ├─request
    │  │              │  │  │  │  └─response
    │  │              │  │  │  ├─entity
    │  │              │  │  │  ├─repository
    │  │              │  │  │  └─service
    │  │              │  │  ├─comment
    │  │              │  │  │  ├─controller
    │  │              │  │  │  ├─entity
    │  │              │  │  │  ├─repository
    │  │              │  │  │  └─service
    │  │              │  │  └─image
    │  │              │  │      ├─entity
    │  │              │  │      ├─repository
    │  │              │  │      └─service
    │  │              │  ├─imageBoard
    │  │              │  │  └─controller
    │  │              │  └─questionBoard
    │  │              │      ├─board
    │  │              │      │  ├─controller
    │  │              │      │  ├─dto
    │  │              │      │  │  ├─request
    │  │              │      │  │  └─response
    │  │              │      │  ├─entity
    │  │              │      │  ├─repository
    │  │              │      │  └─service
    │  │              │      ├─comment
    │  │              │      │  ├─controller
    │  │              │      │  ├─entity
    │  │              │      │  ├─repository
    │  │              │      │  └─service
    │  │              │      └─image
    │  │              │          ├─entity
    │  │              │          ├─repository
    │  │              │          └─service
    │  │              ├─main
    │  │              └─user
    │  │                  ├─controller
    │  │                  ├─dto
    │  │                  │  ├─requests
    │  │                  │  └─response
    │  │                  ├─entity
    │  │                  ├─repository
    │  │                  └─service
    │  └─resources
    │      ├─static
    │      │  ├─account
    │      │  ├─logo
    │      │  ├─mobile
    │      │  └─navbar
    │      └─templates
    │          ├─admin
    │          ├─boards
    │          │  ├─freeBoard
    │          │  ├─imageBoard
    │          │  └─questionBoard
    │          ├─layouts
    │          ├─main
    │          ├─message
    │          └─user
    │              └─mypage
    └─test
        └─java
            └─com
                └─example
                    └─forum
