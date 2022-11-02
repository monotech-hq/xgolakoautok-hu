
(ns app.views.frontend.error-screen.config)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def ERROR-CONTENT {:no-connection      {:title  :yo-do-not-have-internet-connection
                                         :helper :please-check-your-internet-connection
                                         :icon   :wifi_off}
                    :no-permission      {:title  :you-do-not-have-permission-to-view-this-page
                                         :icon   :warning_amber}
                    :page-not-found     {:title  :page-is-not-available
                                         :helper :the-link-you-followed-may-be-broken
                                         :icon   :self_improvement}
                    :under-construction {:title  :page-is-under-construction
                                         :icon   :self_improvement}
                    :under-maintenance  {:title  :page-is-under-maintenance
                                         :helper :please-check-back-soon...
                                         :icon   :self_improvement}})
