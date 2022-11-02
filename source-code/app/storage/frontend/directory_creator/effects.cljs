
(ns app.storage.frontend.directory-creator.effects
    (:require [app.storage.frontend.directory-creator.events     :as directory-creator.events]
              [app.storage.frontend.directory-creator.queries    :as directory-creator.queries]
              [app.storage.frontend.directory-creator.validators :as directory-creator.validators]
              [app.storage.frontend.directory-creator.views      :as directory-creator.views]
              [engines.item-browser.api                          :as item-browser]
              [re-frame.api                                      :as r :refer [r]]
              [x.app-ui.api                                      :as x.ui]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.directory-creator/load-creator!
  ; @param (keyword)(opt) creator-id
  ; @param (map) creator-props
  ;  {:destination-id (string)}
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! {...}]
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! :my-creator {...}]
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! {:destination-id "..."}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ creator-id creator-props]]
      {:db       (r directory-creator.events/store-creator-props! db creator-id creator-props)
       :dispatch [:storage.directory-creator/render-dialog! creator-id]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.directory-creator/create-directory!
  (fn [{:keys [db]} [_ creator-id]]
      (let [directory-name (get-in db [:storage :directory-creator/meta-items :directory-name])
            query          (r directory-creator.queries/get-create-directory-query          db creator-id directory-name)
            validator-f   #(r directory-creator.validators/create-directory-response-valid? db creator-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch-n [[:ui/remove-popup! :storage.directory-creator/view]
                         [:pathom/send-query! :storage.directory-creator/create-directory!
                                              {:query query :validator-f validator-f
                                               :on-success [:storage.directory-creator/directory-created         creator-id]
                                               :on-failure [:storage.directory-creator/directory-creation-failed creator-id]}]]})))

(r/reg-event-fx :storage.directory-creator/directory-created
  (fn [{:keys [db]} [_ creator-id server-response]]
      ; Ha a sikeres mappalétrehozás után még a célmappa az aktuálisan böngészett elem,
      ; akkor újratölti a listaelemeket.
      (let [browser-id     (get-in db [:storage :directory-creator/meta-items :browser-id])
            destination-id (get-in db [:storage :directory-creator/meta-items :destination-id])]
           (if (r item-browser/browsing-item? db browser-id destination-id)
               [:item-browser/reload-items! browser-id]))))

(r/reg-event-fx :storage.directory-creator/directory-creation-failed
  (fn [_ [_ creator-id server-response]]
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/render-bubble! {:body :failed-to-create-directory}]]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.directory-creator/render-dialog!
  (fn [{:keys [db]} [_ creator-id]]
      [:ui/render-popup! :storage.directory-creator/view
                         {:content [directory-creator.views/view creator-id]}]))
