
(ns site.components.frontend.language-selector.effects
    (:require [re-frame.api     :as r :refer [r]]
              [x.router.api     :as x.router]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :components.language-selector/init-selector!
  (fn [{:keys [db]} _]
      (if-let [language (r x.router/get-current-route-path-param db :language)]
              [:x.locales/select-language! (keyword language)]
              [:components.language-selector/detect-language!])))

(r/reg-event-fx :components.language-selector/detect-language!
  (fn [{:keys [db]} _]
      [:x.router/go-to! "/hu"]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :components.language-selector/select-language!
  ; @param (keyword) language
  (fn [{:keys [db]} [_ language]]
      (let [route-string (r x.router/get-current-route-template db)
            swap-route   (r x.router/use-path-params db route-string {:language (name language)})]
           {:dispatch-n [[:x.router/swap-to!          swap-route]
                         [:x.locales/select-language! language]]})))
