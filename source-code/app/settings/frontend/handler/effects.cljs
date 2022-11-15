
(ns app.settings.frontend.handler.effects
    (:require [app.settings.frontend.handler.queries :as handler.queries]
              [re-frame.api                          :as r :refer [r]]
              [x.user.api                            :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.handler/update-user-settings!
  ; @param (map) user-settings
  ;
  ; @usage
  ;  [:settings.handler/update-user-settings! {:my-settings-item "My value"}]
  (fn [{:keys [db]} [_ user-settings]]
      (let [query (r handler.queries/get-update-user-settings-query db user-settings)]
           {:db       (r x.user/set-user-settings! db user-settings)
            :dispatch [:pathom/send-query! :settings.handler/update-user-settings!
                                           {:query query}]})))
