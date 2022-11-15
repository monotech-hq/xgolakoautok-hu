
(ns app.settings.frontend.remove-stored-cookies.effects
    (:require [re-frame.api                         :as r :refer [r]]
              [settings.remove-stored-cookies.views :as remove-stored-cookies.views]
              [x.dictionary.api                     :as x.dictionary]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings/remove-stored-cookies!
  (fn [{:keys [db]} _]
      (let [message (r x.dictionary/look-up db :just-a-moment...)]
           {:dispatch-later [{:ms   0 :dispatch [:x.ui/set-shield! {:content message}]}
                             {:ms  50 :dispatch [:x.environment/remove-cookies!]}
                             {:ms 500 :dispatch [:boot-loader/refresh-app!]}]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.remove-stored-cookies/render-dialog!
  [:x.ui/render-popup! :settings.remove-stored-cookies/view
                       {:body   #'remove-stored-cookies.views/body
                        :header #'remove-stored-cookies.views/header}])
