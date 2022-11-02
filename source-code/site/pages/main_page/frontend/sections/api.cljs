
(ns site.pages.main-page.frontend.sections.api
    (:require [site.pages.main-page.frontend.sections.contacts :as contacts]
              [site.pages.main-page.frontend.sections.hero     :as hero]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(def contacts contacts/view)

(def hero     hero/view)
