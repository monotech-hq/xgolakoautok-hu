
(ns app.storage.frontend.media-viewer.effects
    (:require [app.storage.frontend.media-viewer.events  :as media-viewer.events]
              [app.storage.frontend.media-viewer.queries :as media-viewer.queries]
              [app.storage.frontend.media-viewer.views   :as media-viewer.views]
              [re-frame.api                              :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-viewer/load-viewer!
  ; @param (keyword)(opt) viewer-id
  ; @param (map) viewer-props
  ;  {:current-item (string)(opt)
  ;   :directory-id (string)}
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! {...}]
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! :my-viewer {...}]
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! {:current-item "my-image.png"
  ;                                       :directory-id "my-directory"}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ viewer-id viewer-props]]
      {:db         (r media-viewer.events/load-viewer! db viewer-id viewer-props)
       :dispatch-n [[:storage.media-viewer/render-viewer!          viewer-id]
                    [:storage.media-viewer/request-directory-item! viewer-id]]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-viewer/request-directory-item!
  (fn [{:keys [db]} [_ viewer-id]]
      [:pathom/send-query! :storage.media-viewer/request-directory-item!
                           {:display-progress? true
                            :on-success [:storage.media-viewer/receive-directory-item! viewer-id]
                            :query (r media-viewer.queries/get-request-directory-item-query db viewer-id)}]))

(r/reg-event-fx :storage.media-viewer/render-viewer!
  (fn [_ [_ viewer-id]]
      [:x.ui/render-popup! :storage.media-viewer/view
                           {:content [media-viewer.views/view viewer-id]}]))
