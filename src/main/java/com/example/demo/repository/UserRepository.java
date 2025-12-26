public interface PredictionRuleRepository extends JpaRepository<PredictionRule, Long> {
    Optional<PredictionRule> findByRuleName(String ruleName);
}
