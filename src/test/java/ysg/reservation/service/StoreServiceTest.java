package ysg.reservation.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ysg.reservation.repository.ReservationRepository;
import ysg.reservation.repository.StoreRepository;
import ysg.reservation.repository.MemberRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    private StoreService storeService;










}
