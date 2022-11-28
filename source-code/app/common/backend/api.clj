
(ns app.common.backend.api
    (:require [layouts.popup-a.api]
              [layouts.popup-b.api]
              [layouts.surface-a.api]
              [app.common.backend.documents.prototypes :as documents.prototypes]
              [app.common.backend.links.helpers        :as links.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.common.backend.document.prototypes
(def get-document-prototype        documents.prototypes/get-document-prototype)
(def added-document-prototype      documents.prototypes/added-document-prototype)
(def updated-document-prototype    documents.prototypes/updated-document-prototype)
(def duplicated-document-prototype documents.prototypes/duplicated-document-prototype)
(def updated-edn-prototype         documents.prototypes/updated-edn-prototype)

; app.common.backend.links.helpers
(def valid-link     links.helpers/valid-link)
(def link-reserved? links.helpers/link-reserved?)
