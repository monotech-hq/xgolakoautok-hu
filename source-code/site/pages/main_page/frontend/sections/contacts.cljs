
(ns site.pages.main-page.frontend.sections.contacts)

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- contacts
  []
  [:div {:class :sp-section-body}])

(defn view
  []
  [:section {:id :sp-contacts}
            [contacts]])
