# Application "pets"
Hi, if you ever needed to calculate statistics for different pets based on their tracker types, hope 
this application helps you :)

## Running the app
Please run a configuration with the [Application.java](src%2Fmain%2Fjava%2Fcom%2Ftractive%2Fpets%2FApplication.java) 
as the main class in your IDE. It's enough.

## Running tests
There are two classes for tests:
1. [PetServiceImplTest.java](src%2Ftest%2Fjava%2Fcom%2Ftractive%2Fpets%2Fservice%2Fimpl%2FPetServiceImplTest.java)
contains Unit tests for the implementation of the 
[PetService.java](src%2Fmain%2Fjava%2Fcom%2Ftractive%2Fpets%2Fservice%2FPetService.java) interface.
2. [PetControllerIntegrationTest.java](src%2Ftest%2Fjava%2Fcom%2Ftractive%2Fpets%2Fservice%2Fimpl%2FPetControllerIntegrationTest.java)
contains integration tests for main use cases.

You don't need to set anything additionally. Just run it in your IDE.

## Usage

### Save data
For saving a new pet please use an endpoint POST `/api/v1/pet` with the JSON for dogs
```json
{
    "name": "Leni",
    "type": "DOG",
    "trackerType": "MEDIUM",
    "owner": 1,
    "inZone": true
}
```
or for cats
```json
{
    "name": "Peach",
    "type": "CAT",
    "trackerType": "SMALL",
    "owner": 1,
    "inZone": true,
    "lostTracker": false
}
```
There are two pet types `CAT` and `DOG` and three tracker types `SMALL`, `MEDIUM`, `BIG`. Please be careful, 
the `MEDIUM` tracker type is not supported for cats. 

If you don't know any info about `inZone` and `lostTracker` (for cats) parameters, please don't set it. Also, 
if your pet doesn't yet have a name or an owner, feel free to leave it too. After saving a pet you get an object 
with the `id` in it. 

For saving data from trackers (`inZone` and `lostTracker` (for cats) parameters) please use an endpoint 
PATCH `/api/v1/pet/{id}`with the JSON:
```json
{
    "inZone": true,
    "lostTracker": true
}
```
You can also send only one of the parameters. Other parameters are not supported by the current API version.

### Query data
If you lost an id of your pet, you can always use an endpoint GET `/api/v1/pet` without any parameters, then you 
get a list of all pets and can find your lost pet it in.

### Get statistics
For getting information about the number of pets currently outside the power saving zone grouped by pet type and
tracker type please use an endpoint GET `/api/v1/pet/statistics` without any parameters. 

## Implementation details
Even if it's not the best practice, I tool a decision to use `@Entity` files as DTOs in REST API calls. I did that
in order to save time and not create additional classes, because it's a small test project and there's no other 
logic or annotations for fields needed. 

## Possible improvements 
1. Using another DB, if we have a lot of data, some column-based DB would be perfect.
2. Using not REST API calls but a message broker (Kafka, RabbitMQ, etc.) for receiving data.
3. Saving tracking data in a bulk scheduled operation e.g. once per a second.
4. Saving timestamps to be able to have a history log of changing states of a pet in time.
5. Add more filters and `group by` options, change implementation to be it more flexible and support more cases easily.
6. Add SwaggerUI and provide some docs for using APIs.

... and so on :)

## Work log
* Setting up the application - 0.5h
* Writing source code - 1.5h
* Writing unit and integration tests - 1.5h
* Polishing everything up and writing readme - 0.5h

Total: 4h

## Feedback
I would be happy to discuss your feedback and consider implementation details in case of new features :)