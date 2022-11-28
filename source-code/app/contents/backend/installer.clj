
(ns app.contents.backend.installer
    (:require [app.common.backend.api :as common]
              [lorem-ipsum.api        :as lorem-ipsum]
              [mongo-db.api           :as mongo-db]
              [x.core.api             :as x.core]
              [x.user.api             :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  ; @return (namespaced map)
  []
  (let [request    {:session x.user/SYSTEM-USER-ACCOUNT}
        prepare-f #(common/added-document-prototype request %)]
       (mongo-db/insert-document! "contents" {:content/name       "Lorem Ipsum"
                                              :content/body       lorem-ipsum.api/LONG
                                              :content/visibility :public}
                                             {:prepare-f prepare-f})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
