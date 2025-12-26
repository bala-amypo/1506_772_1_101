@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public User register(UserRegisterDto dto) {

        if (dto.getName() == null || dto.getName().isBlank())
            throw new IllegalArgumentException("Name cannot be empty");

        if (dto.getPassword() == null || dto.getPassword().isBlank())
            throw new IllegalArgumentException("Password cannot be empty");

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email format");

        if (repo.findByEmail(dto.getEmail()).isPresent())
            throw new IllegalArgumentException("Email already exists");

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .roles(
                    dto.getRoles() == null || dto.getRoles().isEmpty()
                        ? Set.of(Role.ROLE_USER)
                        : dto.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet())
                )
                .createdAt(LocalDateTime.now())
                .build();

        return repo.save(user);
    }
}
