# footballBets: API Evolution and Versioning Strategies

A Spring Boot REST API for International Football Bets

# How to make Changes in the REST API

## 1. API Evolution (Mostly no breaking Changes)

### Resource Changes

#### Data Type Change: Mostly no Breaking Change,

Strategies:

- Only make changes to Domain Model, DAO and Service, leave the DTO unaffected
- add a new field in the presentation layer DTO, and deprecate the old one for a transitional period
-

#### Add,Remove or Modify a Field in the Domain Model

Strategies: Try to make the field optional or mark field as @deprecated

- Add: If Mandatory as Input Resource, it is a Breaking Change; However, you can make the field non-mandatory
- Remove,Modify: non-breaking change when dealing with optional fields . @deprecate field, otherwise breaking change for
  either input or output or both

#### Structure Changes (e.g. the Domain Model separates some field to another entity)

Strategies:

- Duplicate the DTO and the POST-Method in the Controller, set Old DTO to @Deprecated , set a Custom Mime Type to
  new POST-Method (CONTENT NEGOTIATION VERSIONING)

```java 

@Deprecated
@Schema(deprecated = true)
public class OldCompetitionDto implements Serializable {

    @PostMapping(produces = "application/vnd.kirschning.new-comp+json", consumes = "application/vnd.kirschning.new-comp+json")
    public NewCompetitionDto post(@RequestBody @Valid ONewCompetitionDto newCompetitionDto) {

    }
}
```

```txt
The Evolution Strategy / Versionless API

Roughly speaking, the methodologies and suggestions we followed throughout this lesson are known as the “evolution” strategy.

Instead of versioning the whole API or Resources, the API is in constant evolution, maintaining the functionality as much as possible, 
marking single fields as deprecated when necessary, and letting clients adapt to the changes and evolve together with the interface.

The objective of maintaining backward compatibility and avoiding introducing potentially breaking changes is a key aspect of this technique.
```

### URL Changes

#### Replace URL path

Strategies:
Establish a second path (here: sportbets) and
keep both paths functional for a period of time

```java 

@RestController
@RequestMapping(value = {"/tipps", "/sportbets"})
public class WorkerController {

    // ….
}
```

#### Change Number of @PathVariable or @RequestParam or modify them

Strategies:
Setting the “required” attribute to “false”

```java 

@RestController
public class TippsController {

    @GetMapping(value = {"/tipps", "tipper/{tipperId}/tipps"})
    public List<TaskDto> searchTipps(
            @PathVariable(required = false) Long tipperId,
            @RequestParam(required = false) String compRound,
            @RequestParam(required = false) Long competitionId) {
        //…
    }
    // ...
}
```

## 2. API Versioning (Breaking Changes: Avoid API versioning as much as possible.)

Versioning these days is done by either URL versioning or Content Negotiation Versioning

### URL Versioning

Here, we’ll specify the version in the URL path variable:

```txt

https://www.example.com/api/v1/resource
https://www.example.com/api/v2/resource
```

### Header Versioning

Here, the client can point out the version they want to consume using a request header,
which can be a **custom header** or the standard **Accept and Content-Type headers**
commonly used for Content Negotiation.

```txt

--------------------------
GET-Request:
http
GET /api/user/123 HTTP/1.1
Host: example.com
Accept: application/vnd.kirschning.api.v2+json
----------------------------
POST Request:
http
POST /api/user HTTP/1.1
Host: example.com
Content-Type: application/json
Accept: application/vnd.kirschning.api.v1+json
```

#### A custom header might look as follows:

```txt
“API-version: 1”
“API-version: 2”
```

Naturally, with header versioning, we can use the same URL for multiple versions of API.



###  Code Examples for API Versioning and Header Versioning
Scenario: A mandatory field has been added to the Domain Model, breaking the Contract of the API in parts.
Changes have to be made in the Controller (and DTO )
####  API Versioning 
Establish a new Package (v2) and copy the Controller class into it. Provide the following Modifications:
Give the Controller a distinct name, so the spring bean context can distinguish and load both versions.
```java

@RestController(value = "MatchdayController.url.v2")
@RequestMapping(value = "/v2/matchdays")
public class MatchdayController {
    
}
```
####  Header Versioning 
```java

@RestController(value = "MatchdayController.contentnegotiation.v2")
@RequestMapping(value = "/matchdays", produces = "application/vnd.kirschning.api.v2+json")

public class MatchdayController {

  @PostMapping(consumes = "application/vnd.kirschning.api.v2+json")
  @ResponseStatus(HttpStatus.CREATED)
  public MatchdayDto create(@RequestBody @Valid MatchdayDto newMatchday) {
    //......
     }

  }
```