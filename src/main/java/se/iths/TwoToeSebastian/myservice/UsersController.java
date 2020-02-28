package se.iths.TwoToeSebastian.myservice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RequestMapping("/api/v1/usersData")   // link from insomnia after 8080/
@RestController
public class UsersController {

    final UserDataRepository repository;
    private final UserDataModelAssembler assembler;

    public UsersController(UserDataRepository in, UserDataModelAssembler in2) {
        this.repository = in;
        this.assembler = in2;
    }

    @GetMapping
    public CollectionModel<EntityModel<UserData>> all() {
        log.info("All persons called");
        return assembler.toCollectionModel(repository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<UserData>> one(@PathVariable Integer id) {
        log.info("One person called");
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserData>> createPerson(@RequestBody UserData user) {
        log.info("Created " + user);

        if(repository.findById(user.getId()).isPresent()) {  // if id exist, check for not overwrite
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        var p = repository.save(user);          // Sparar in användare från request body in i lista i databas
        log.info("Saved to repository " + p);
        var entityModel = assembler.toModel(p);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
      /*
        HttpHeaders headers = new HttpHeaders();        // skapar en header
        headers.setLocation(linkTo(UsersController.class).slash(p.getId()).toUri());    // autogenera ny user id,  //headers.add("Location", "/api/persons/" + p.getId());
        return new ResponseEntity<>(p, headers, HttpStatus.CREATED); // p body(user data in), headers vilka headers som skickas tillbaka, httpstatus.created (201 ok kod).
     */
    }

    @PutMapping("/{id}")
    ResponseEntity<EntityModel<UserData>> replacePerson(@RequestBody UserData userIn, @PathVariable Integer id) {

        if(id == userIn.id){
            var p = repository.findById(id)
                    .map(existingUser -> {
                            existingUser.setUserName(userIn.getUserName());
                            existingUser.setRealName(userIn.getRealName());
                            existingUser.setCity((userIn.getCity()));
                            existingUser.setIncome(userIn.getIncome());
                            existingUser.setInRelationship(userIn.inRelationship);
                        repository.save(existingUser);
                        return existingUser;})
                    .get();

            var entityModel = assembler.toModel(p);
            return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }









}
