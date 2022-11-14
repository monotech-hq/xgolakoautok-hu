
(ns site.xgo.pages.main-page.frontend.sections.api
    (:require [site.xgo.pages.main-page.frontend.sections.contacts       :as contacts]
              [site.xgo.pages.main-page.frontend.sections.hero           :as hero]
              [site.xgo.pages.main-page.frontend.sections.categories.api :as categories]
              [site.xgo.pages.main-page.frontend.sections.models.api     :as models]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(def hero       hero/view)

(def categories categories/view)

(def models     models/view)

(def contacts   contacts/view)
