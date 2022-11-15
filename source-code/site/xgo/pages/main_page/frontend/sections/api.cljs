
(ns site.xgo.pages.main-page.frontend.sections.api
    (:require [site.xgo.pages.main-page.frontend.sections.contacts       :as contacts]
              [site.xgo.pages.main-page.frontend.sections.hero           :as hero]
              [site.xgo.pages.main-page.frontend.sections.categories.api :as categories]
              [site.xgo.pages.main-page.frontend.sections.models.api     :as models]
              [site.xgo.pages.main-page.frontend.sections.types.api     :as types]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(def hero       hero/view)

(def categories categories/view)

(def models     models/view)

(def types      types/view)

(def contacts   contacts/view)
