
(ns app.storage.frontend.file-uploader.effects
    (:require [app.storage.frontend.file-uploader.events       :as file-uploader.events]
              [app.storage.frontend.file-uploader.helpers      :as file-uploader.helpers]
              [app.storage.frontend.file-uploader.queries      :as file-uploader.queries]
              [app.storage.frontend.file-uploader.side-effects :as file-uploader.side-effects]
              [app.storage.frontend.file-uploader.validators   :as file-uploader.validators]
              [app.storage.frontend.file-uploader.subs         :as file-uploader.subs]
              [app.storage.frontend.file-uploader.views        :as file-uploader.views]
              [dom.api                                         :as dom]
              [engines.item-browser.api                        :as item-browser]
              [re-frame.api                                    :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.file-uploader/load-uploader!
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :browser-id (keyword)
  ;   :destination-id (string)}
  ;
  ; @usage
  ;  [:storage.file-uploader/load-uploader! {...}]
  ;
  ; @usage
  ;  [:storage.file-uploader/load-uploader! :my-uploader {...}]
  ;
  ; @usage
  ;  [:storage.file-uploader/load-uploader! {:allowed-extensions ["htm" "html" "xml"]
  ;                                          :destination-id "my-directory"}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ uploader-id uploader-props]]
      ; - Az egyes fájlfeltöltési folyamatok a fájlfeltöltő ablak bezáródása után még a fájl(ok)
      ;   méretétől függően NEM azonnal fejeződnek be.
      ; - Az uploader-id egyedi azonosító alkalmazása lehetővé teszi, hogy az egy időben történő
      ;   különböző fájlfeltöltések kezelhetők legyenek.
      ; - A request-id azonosító feltöltési folyamatonként eltérő kell legyen, ehhez szükséges,
      ;   hogy az uploader-id azonosító is ... eltérő legyen!
      {:db (r file-uploader.events/store-uploader-props! db uploader-id uploader-props)
       :fx [:storage.file-uploader/open-file-selector! uploader-id uploader-props]}))

(r/reg-event-fx :storage.file-uploader/cancel-uploader!
  (fn [{:keys [db]} [_ uploader-id]]
      {:db       (r file-uploader.events/clean-uploader! db uploader-id)
       :dispatch [:x.ui/remove-popup! :storage.file-uploader/view]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.file-uploader/start-progress!
  (fn [{:keys [db]} [_ uploader-id]]
      (let [query        (r file-uploader.queries/get-upload-files-query          db uploader-id)
            form-data    (r file-uploader.subs/get-form-data                      db uploader-id)
            validator-f #(r file-uploader.validators/upload-files-response-valid? db uploader-id %)]
           {:dispatch-n [[:storage.file-uploader/progress-started uploader-id]
                         [:pathom/send-query! (file-uploader.helpers/request-id uploader-id)
                                              {:body       (dom/merge-to-form-data! form-data {:query query})
                                               :on-success [:storage.file-uploader/progress-successed uploader-id]
                                               :on-failure [:storage.file-uploader/progress-failured  uploader-id]
                                               :validator-f validator-f}]]})))

(r/reg-event-fx :storage.file-uploader/files-selected-to-upload
  (fn [{:keys [db]} [_ uploader-id]]
      ; A storage--file-selector input on-change eseménye indítja el a feltöltés inicializálását.
      (if-let [any-file-selected? (file-uploader.side-effects/any-file-selected?)]
              {:db       (r file-uploader.events/init-uploader! db uploader-id)
               :dispatch [:storage.file-uploader/render-uploader! uploader-id]})))

(r/reg-event-fx :storage.file-uploader/progress-started
  (fn [_ [_ uploader-id]]
      {:dispatch-n [[:storage.file-uploader/render-progress-notification! uploader-id]
                    [:x.ui/remove-popup! :storage.file-uploader/view]]}))

(r/reg-event-fx :storage.file-uploader/progress-successed
  (fn [{:keys [db]} [_ uploader-id]]
      ; - XXX#5087
      ;   Az egyes feltöltési folyamatok befejezése/megszakadása után késleltetve zárja le az adott
      ;   feltöltőt, így a felhasználónak van ideje észlelni a visszajelzést.
      ; - Ha a sikeres fájlfeltöltés után még a célmappa az aktuálisan böngészett elem,
      ;   akkor újratölti a listaelemeket.
      (let [browser-id     (get-in db [:storage :file-uploader/meta-items uploader-id :browser-id])
            destination-id (get-in db [:storage :file-uploader/meta-items uploader-id :destination-id])]
           {:dispatch-later [{:ms 8000 :dispatch [:storage.file-uploader/end-uploader! uploader-id]}]
            :dispatch-if    [(r item-browser/browsing-item? db browser-id destination-id)
                             [:item-browser/reload-items! browser-id]]})))

(r/reg-event-fx :storage.file-uploader/progress-failured
  (fn [_ [_ uploader-id]]
      {; XXX#5087
       :dispatch-later [{:ms 8000 :dispatch [:storage.file-uploader/end-uploader! uploader-id]}]}))

(r/reg-event-fx :storage.file-uploader/end-uploader!
  (fn [{:keys [db]} [_ uploader-id]]
      ; - A feltöltő lezárása után késleltetve törli ki annak adatait, hogy a még
      ;   látszódó folyamatjelző számára elérhetők maradjanak az adatok.
      ; - Ha a felöltő lezárásakor nincs aktív feltöltési folyamat, akkor bezárja a folyamatjelzőt.
      ;   (Az utolsó feltöltési folyamat befejeződése és az utolsó feltöltő lezárása
      ;    közötti időben is indítható új feltöltési folyamat!)
      {:dispatch-later [{:ms 500 :dispatch [:storage.file-uploader/clean-uploader! uploader-id]}]
       :dispatch-if [(not (r file-uploader.subs/file-upload-in-progress? db))
                     [:x.ui/remove-bubble! :storage.file-uploader/progress-notification]]}))

(r/reg-event-fx :storage.file-uploader/render-uploader!
  (fn [_ [_ uploader-id]]
      [:x.ui/render-popup! :storage.file-uploader/view
                           {:content [file-uploader.views/view uploader-id]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.file-uploader/render-progress-notification!
  [:x.ui/render-bubble! :storage.file-uploader/progress-notification
                        {:body        #'file-uploader.views/progress-notification-body
                         :autoclose?  false
                         :user-close? false}])
