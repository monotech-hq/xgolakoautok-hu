
(ns site.xgo.pages.main-page.frontend.sections.categories.effects
  (:require [re-frame.api         :as r]
            [normalize.api :as normalize]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-event-fx
  :categories/select!
  (fn [_ [_ name]]
    {:dispatch-n [[:x.db/set-item! [:filters] {:category (normalize/clean-text name "-+")}]]
     :url/set-url! (str "/" (normalize/clean-text name "-+"))}))
