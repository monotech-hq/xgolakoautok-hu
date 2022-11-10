
(ns site.xgo.pages.main-page.frontend.sections.api
    (:require [site.xgo.pages.main-page.frontend.sections.contacts         :as contacts]
              [site.xgo.pages.main-page.frontend.sections.hero             :as hero]
              [site.xgo.pages.main-page.frontend.sections.categories.views :as categories]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(def hero       hero/view)

(def categories categories/view)

(def contacts   contacts/view)
