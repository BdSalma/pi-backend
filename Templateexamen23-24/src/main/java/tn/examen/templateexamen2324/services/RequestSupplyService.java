package tn.examen.templateexamen2324.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.*;
import tn.examen.templateexamen2324.entity.Individu;
import tn.examen.templateexamen2324.entity.RequestSupply;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j

@Service
public class RequestSupplyService implements RequestSupplyIService{
    @Autowired
    RequestSupplyRepository requestSupplyRepository;
    @Autowired
    SocietyRepository societyRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    DevisRepository devisRepository;
    @Autowired
    ForumService fs;
    @Autowired
    IndividuRepository individuRepo;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Override
    public RequestSupply addRequestSupply(RequestSupply requestSupply, String idU) {
        LocalDate currentDate = LocalDate.now();
        LocalDate requestDate = requestSupply.getDate();
        int validityDays = requestSupply.getValidity();

        if (currentDate.plusDays(validityDays).isAfter(requestDate)) {
            throw new IllegalArgumentException("Request date must be at least " + validityDays + " days in the future.");
        }

        Individu i = individuRepo.findById(idU).orElse(null);
        if (i instanceof Individu) {
            requestSupply.setIndividu(i);
        } else {
            System.out.println("Individu n'est pas un user");
        }

        requestSupply.setForum(fs.getCurrentForum());
        return requestSupplyRepository.save(requestSupply);
    }


    @Override
    public List<RequestSupply> retrieveAllRequestSupplies() {
        List<RequestSupply> allSupplies = requestSupplyRepository.findAll();
        return allSupplies.stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Running)
                .collect(Collectors.toList());
    }

    @Override
    public RequestSupply retrieveRequestSupplyById(int idRequestSupply) {
        return requestSupplyRepository.findById(idRequestSupply).orElse(null);
    }

    @Override
    public void deleteRequestSupply(int idRequestSupply) {

        this.requestSupplyRepository.deleteById(idRequestSupply);
    }

    @Override
    public RequestSupply updateRequestSupply(int idRequestSupply) {
        RequestSupply r = this.requestSupplyRepository.findById(idRequestSupply).orElse(null);
        return this.requestSupplyRepository.save(r);
    }
    @Override
    public List<RequestSupply> getRequestSupplyByIndividus(String idU) {
        Individu i = individuRepo.findById(idU).orElse(null);
        if (i == null) {
            return Collections.emptyList(); // Return empty list if Individu not found
        }

        return i.getRequestSupplies().stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Running)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestSupply> getOLdRequestSupplyByIndividus(String idU) {
        Individu i = individuRepo.findById(idU).orElse(null);
        if (i == null) {
            return Collections.emptyList();
        }

        return i.getRequestSupplies().stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Archived)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestSupply> recommendNewRequestsForSociety(String societyId) {
        // Step 1: Retrieve all the accepted devis for the specified society
        List<Devis> acceptedDevis = devisRepository.findBySocietyDevisId(societyId);

        // Step 2: Retrieve all the requests of the individus that have old devis accepted for the specified society
        List<RequestSupply> newRequestsFromIndividus = acceptedDevis.stream()
                .map(Devis::getRequestSupply)
                .filter(Objects::nonNull)
                .map(RequestSupply::getIndividu)
                .filter(Objects::nonNull)
                .flatMap(individu -> requestSupplyRepository.findAllByIndividuIdAndDevisIsNull(individu.getId()).stream())
                .distinct()
                .collect(Collectors.toList());

        return newRequestsFromIndividus.stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Running)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestSupply> getRequestSupplyByForumTheme(String theme) {
        List<RequestSupply> allSupplies = requestSupplyRepository.findAll();
        return allSupplies.stream()
                .filter(r -> r.getForum() != null && r.getForum().getTheme().equals(theme))
                .collect(Collectors.toList());
    }
    @Override
    public Map<String, Integer> getIndividuStatistics(String idU) {
        Map<String, Integer> statistics = new HashMap<>();

        // Retrieve all running requests for the individu
        List<RequestSupply> runningRequests = getRequestSupplyByIndividus(idU);

        // Calculate the number of running requests
        int numRunningRequests = runningRequests.size();
        statistics.put("RunningRequests", numRunningRequests);

        // Calculate the number of accepted devis and refused devis
        int numAcceptedDevis = 0;
        int numRefusedDevis = 0;
        for (RequestSupply request : runningRequests) {
            for (Devis devis : request.getDevis()) {
                if (devis.getStatus() == DevisStatus.Accepted) {
                    numAcceptedDevis++;
                } else if (devis.getStatus() == DevisStatus.refused) {
                    numRefusedDevis++;
                }
            }
        }
        statistics.put("AcceptedDevis", numAcceptedDevis);
        statistics.put("RefusedDevis", numRefusedDevis);

        // Calculate the number of accepted invoices and refused invoices
        int numAcceptedInvoices = 0;
        int numRefusedInvoices = 0;
        for (RequestSupply request : runningRequests) {
            if (request.getInvoice() != null) {
                if (request.getInvoice().getStatus() == InvoiceStatus.Accepted) {
                    numAcceptedInvoices++;
                } else if (request.getInvoice().getStatus() == InvoiceStatus.refused) {
                    numRefusedInvoices++;
                }
            }
        }
        statistics.put("AcceptedInvoices", numAcceptedInvoices);
        statistics.put("RefusedInvoices", numRefusedInvoices);
        log.info(String.valueOf(numRunningRequests));
        log.info(String.valueOf(numRefusedInvoices));

        return statistics;
    }
    @Override
    public Map<String, Integer> getSocietyStatistics(String societyId) {
        Map<String, Integer> statistics = new HashMap<>();

        // Retrieve the society
        Society society = societyRepo.findById(societyId).orElse(null);
        if (society == null) {
            log.error("Society with ID {} not found", societyId);
            return statistics;
        }

        // Retrieve all devis associated with the society
        List<Devis> societyDevis = devisRepository.findBySocietyDevisId(societyId);

        // Filter out devis associated with running requests
        List<Devis> runningDevis = societyDevis.stream()
                .filter(devis -> devis.getRequestSupply().getStatus() == RequestSupplyStatus.Running)
                .collect(Collectors.toList());

        // Calculate the number of accepted and refused devis
        int numAcceptedDevis = 0;
        int numRefusedDevis = 0;
        for (Devis devis : runningDevis) {
            if (devis.getStatus() == DevisStatus.Accepted) {
                numAcceptedDevis++;
            } else if (devis.getStatus() == DevisStatus.refused) {
                numRefusedDevis++;
            }
        }
        statistics.put("AcceptedDevis", numAcceptedDevis);
        statistics.put("RefusedDevis", numRefusedDevis);

        // Calculate the number of accepted and refused invoices
        int numAcceptedInvoices = 0;
        int numRefusedInvoices = 0;
        for (Devis devis : runningDevis) {
            Invoice invoice = devis.getRequestSupply().getInvoice();
            if (invoice != null) {
                if (invoice.getStatus() == InvoiceStatus.Accepted) {
                    numAcceptedInvoices++;
                } else if (invoice.getStatus() == InvoiceStatus.refused) {
                    numRefusedInvoices++;
                }
            }
        }

        statistics.put("AcceptedInvoices", numAcceptedInvoices);
        statistics.put("RefusedInvoices", numRefusedInvoices);

        log.info("Statistics for society {}: {}", societyId, statistics);
        return statistics;
    }
    // Scheduled task to check for request expiry every hour
    @Scheduled(cron = "0 */2 * * * *") // Run every 5 minutes
    public void checkRequestExpiryAndArchive() {
        LocalDate now = LocalDate.now();
        LocalDate twentyFourHoursAgo = now.minusDays(1);
        List<RequestSupply> expiringRequests = requestSupplyRepository.findByDateBeforeAndStatus(twentyFourHoursAgo, RequestSupplyStatus.Running);

        for (RequestSupply request : expiringRequests) {
            // Log expiring requests
            log.info("Demande #" + request.getIdRequestSupply() + " sur le point d'expiration");

            // Check if the request is older than 24 hours
            LocalDate requestCreationTime = request.getDate();
            if (requestCreationTime.isBefore(twentyFourHoursAgo)) {
                // Archive the request
                request.setStatus(RequestSupplyStatus.Archived);
                requestSupplyRepository.save(request);
                // Log archived request
                log.info("Demande #" + request.getIdRequestSupply() + " archivée après 24 heures.");
            }
        }
    }

        // Method to retrieve requests without devis and close to expiration
        public List<RequestSupply> getRequestsWithoutDevisAndCloseToExpiration() {
            LocalDate now = LocalDate.now();
            // Define your criteria to retrieve requests without devis and close to expiration
            // For example, requests that have no devis and are expiring in the next 3 days
            return requestSupplyRepository.findRequestsWithoutDevisAndCloseToExpiration(now.plusDays(3));
        }

    // Scheduled task to send emails for requests without devis and close to expiration
    @Scheduled(cron = "0 0 9 * * ?") // Run every day at 9:00 AM
    public void sendEmailsForRequestsWithoutDevisAndCloseToExpiration() {
        List<RequestSupply> requests = getRequestsWithoutDevisAndCloseToExpiration();

        for (RequestSupply request : requests) {
            // Send email to all societies for each request
            sendEmailToAllSocietiesWithRoleFournisseur(request);
        }
    }


    public void sendEmailToAllSocietiesWithRoleFournisseur(RequestSupply request) {
        List<Society> societies = societyRepo.findAllByRole(SocietyRole.Fourniseur);
        for (Society society : societies) {
            sendEmail(request, society);
        }
    }


    private void sendEmail(RequestSupply request, Society society) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(society.getEmail());
            helper.setSubject("Reminder: Request Expiration");
            helper.setText("Dear " + society.getUsername() + ",\n\n"
                    + "This is a reminder that your request #" + request.getIdRequestSupply() + " is expiring soon and has no attached devis.\n\n"
                    + "Please take necessary actions.\n\n"
                    + "Regards,\n"
                    + "Your Application");
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
