package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.repository.*;

@Service
@RequiredArgsConstructor
@Transactional
public class StatisticsService {

    private final ProgramRepository programRepository;
    private final FolderRepository folderRepository;
    private final OfferingRepository offeringRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ArticleRepository articleRepository;

    public long getProgramsCount() {
        return programRepository.count();
    }

    public long getFoldersCount() {
        return folderRepository.count();
    }

    public long getFoldersCountByCategory(FolderCategory folderCategory) {
        return folderRepository.countByCategory(folderCategory);
    }

    public long getOfferingsCount() {
        return offeringRepository.count();
    }

    public long getOrdersCount() {
        return orderRepository.count();
    }

    public long getUsersCount() {
        return userRepository.count();
    }

    public long getEventsCount() {
        return eventRepository.count();
    }

    public long getArticlesCount() {
        return articleRepository.count();
    }

}
