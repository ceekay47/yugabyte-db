// tslint:disable
/**
 * Yugabyte Cloud
 * YugabyteDB as a Service
 *
 * The version of the OpenAPI document: v1
 * Contact: support@yugabyte.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */




/**
 * Default internal tags update data
 * @export
 * @interface DefaultInternalTagsUpdateData
 */
export interface DefaultInternalTagsUpdateData  {
  /**
   * Email prefix of the user
   * @type {string}
   * @memberof DefaultInternalTagsUpdateData
   */
  email_prefix: string;
  /**
   * Value of yb_dept tag
   * @type {string}
   * @memberof DefaultInternalTagsUpdateData
   */
  yb_dept?: string;
  /**
   * Value of yb_task tag
   * @type {string}
   * @memberof DefaultInternalTagsUpdateData
   */
  yb_task?: string;
  /**
   * Whether the update was successful or not
   * @type {boolean}
   * @memberof DefaultInternalTagsUpdateData
   */
  success: boolean;
  /**
   * Error message when update fails
   * @type {string}
   * @memberof DefaultInternalTagsUpdateData
   */
  error?: string;
}



